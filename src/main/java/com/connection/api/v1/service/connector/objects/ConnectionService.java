package com.connection.api.v1.service.connector.objects;

import com.connection.api.v1.feign.admin.AdminService;
import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.objects.Connection;
import com.connection.api.v1.model.connector.objects.ConnectionToken;
import com.connection.api.v1.model.external.Project;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.payload.connector.objects.ConnectionSetupPayload;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionTokenRepository;
import com.connection.constants.Constants;
import com.connection.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    private final AdminService adminService;
    private final ConnectionRepository connectionRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionTokenRepository connectionTokenRepository;
    private final MetadataUtil metadataUtil;

    @Autowired
    public ConnectionService(AdminService adminService,
                             ConnectionRepository connectionRepository,
                             ConnectionTypeRepository connectionTypeRepository,
                             ConnectionTokenRepository connectionTokenRepository,
                             MetadataUtil metadataUtil) {
        this.adminService = adminService;
        this.connectionRepository = connectionRepository;
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionTokenRepository = connectionTokenRepository;
        this.metadataUtil = metadataUtil;
    }

    public Object createConnection(ConnectionSetupPayload payload, String authentication) {

        Connection connection = new Connection();
        connection.setName(payload.getName());
        connection.setProjectId(payload.getProjectId());
        connection.setProperties(payload.getProperties());
        connection.setCustomAttributes(payload.getCustomAttributes());

        ResponseEntity<Object> projectResp = adminService.getProject(connection.getProjectId(), authentication);

        if (projectResp.getStatusCode().isError()) {
            throw new ApiException(new ApiResponse<>(ErrorMessage.PROJECT_NOT_FOUND));
        }
        Project project = (Project) projectResp.getBody();

        connection.setAccountId(project.getOrganizationId());
//        connection.setAccountName(project.getOrganizationName());
//        connection.setOrganizationCode(project.getOrganizationCode());
        connection.setProjectName(project.getName());
        connection.setProjectId(project.getId());
        connection.setProjectCode(project.getProjectCode());

        if (isConnectionNameTaken(payload.getName(), connection.getAccountId(), connection.getProjectId(), false))
            throw new ApiException(new ApiResponse<>(HttpStatus.CONFLICT.value(), ErrorMessage.CONNECTION_NAME_ALREADY_TAKEN));

        Optional<ConnectionType> connectionType = connectionTypeRepository.findById(payload.getConnectionTypeId());

        if (connectionType.isEmpty())
            throw new ApiException(new ApiResponse<>(ErrorMessage.CONNECTION_TYPE_NOT_FOUND));

        connection.setConnectionObjectType(connectionType.get().getConnectionTypeDetails().getConnectionObjectType());
        connection.setConnectionTypeId(connectionType.get().getId());
        connection.setConnectionTypeName(connectionType.get().getName());
//        connection.setShortCode(sequenceService.getNext(Connection.DEFAULT_TYPE, connectionType.get().getTypeDetails().getPrefix()));

        setConnectionPropertiesFromConnectionToken(payload.getConnectionToken(), connection, connectionType.get());

        connection.setConnectionObjectType(connectionType.get().getConnectionTypeDetails().getConnectionObjectType());
        connection.setPrivate(connectionType.get().isPrivate());
        connection.setMetadata(metadataUtil.createMetadata(authentication));
        connection.setNormalizedName(connection.getName().toUpperCase());
        connection.setDeleted(false);
        connection.setInvalid(false);
        connection.setGlobalConnection(connectionType.get().isGlobalConnectionType());
        connection.setVersion(connectionType.get().getVersion());

        return connectionRepository.save(connection);
    }


    private void setConnectionPropertiesFromConnectionToken(String connectionTokenId,
                                                            Connection connection, ConnectionType connectionType) {
        if (connectionType.getConnectionTypeDetails().isConnectionTokenRequired()) {

            Optional<ConnectionToken> connectionToken = connectionTokenRepository.findByIdAndConnectionTypeId(
                    connectionTokenId, connection.getConnectionTypeId());

            if (connectionToken.isEmpty())
                throw new ApiException(new ApiResponse<>(ErrorMessage.INVALID_CONNECTION_TOKEN));

            if (usesSecretManager(connectionType)) {
                connection.getProperties().put(Constants.ACCESS_THROUGH_SECRET, true);
                connection = connectionRepository.save(connection);
                secureCredentials(connection, connectionType, connectionToken.get());
            } else {
                connection.getProperties().putAll(connectionToken.get().getConnectionProperties());
            }

            /*
             * Delete the connection token after all the connection token properties are
             * added to the connection properties.
             */
            connectionTokenRepository.deleteById(connectionTokenId);
        }
    }

    private void secureCredentials(Connection connection, ConnectionType connectionType, ConnectionToken connectionToken) {
        // implement it later
    }

    public boolean isConnectionNameTaken(String connectionName, String accountId, String projectId,
                                         boolean isDeleted) {
        return connectionRepository.findByNameAndAndAccountIdAndProjectIdAndIsDeleted(connectionName,
                accountId, projectId, isDeleted).isPresent();
    }

    private boolean usesSecretManager(ConnectionType connectionType) {
        return (connectionType.getConnectionTypeDetails().getAdditionalInfo().containsKey(Constants.ACCESS_THROUGH_SECRET)
                && connectionType.getConnectionTypeDetails().isConnectionTokenRequired());
    }

    public List<Connection> getConnectionList(String projectId, Pageable pageable) {
            return connectionRepository.findByProjectIdAndIsDeleted(projectId,
                     false, pageable);
    }
}
