package com.connection.api.v1.service.connector.objects;

import com.connection.api.v1.feign.admin.AdminService;
import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.OperationModes;
import com.connection.api.v1.model.connector.objects.Connection;
import com.connection.api.v1.model.connector.objects.ConnectionToken;
import com.connection.api.v1.model.external.Project;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.payload.connector.objects.ConnectionSetupPayload;
import com.connection.api.v1.payload.connector.objects.ConnectionUpdatePayload;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionTokenRepository;
import com.connection.constants.Constants;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import com.connection.exception.ExceptionStackTrace;
import com.connection.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.HashedMap;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionService.class);
    private final AdminService adminService;
    private final ConnectionRepository connectionRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionTokenRepository connectionTokenRepository;
    private final MetadataUtil metadataUtil;
    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public ConnectionService(AdminService adminService,
                             ConnectionRepository connectionRepository,
                             ConnectionTypeRepository connectionTypeRepository,
                             ConnectionTokenRepository connectionTokenRepository,
                             MetadataUtil metadataUtil,
                             BeanFactory beanFactory,
                             ObjectMapper objectMapper) {
        this.adminService = adminService;
        this.connectionRepository = connectionRepository;
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionTokenRepository = connectionTokenRepository;
        this.beanFactory = beanFactory;
        this.objectMapper=objectMapper;
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

        Project project = objectMapper.convertValue(projectResp.getBody() , Project.class);

        connection.setAccountId(project.getOrganizationId());
//        connection.setAccountName(project.getOrganizationId());
//        connection.setCategory("Advertising");
//        connection.setAccountId(project.getOrganizationId());
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

//        setConnectionPropertiesFromConnectionToken(payload.getConnectionToken(), connection, connectionType.get());

        connection.setProperties(new HashedMap<>());

        connection.setConnectionObjectType(connectionType.get().getConnectionTypeDetails().getConnectionObjectType());
        connection.setPrivate(connectionType.get().isPrivate());
        connection.setMetadata(metadataUtil.createMetadata(authentication));
        connection.setNormalizedName(connection.getName().toUpperCase());
        connection.setDeleted(false);
        connection.setInvalid(false);
        connection.setProcessType("BATCH");
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

    public Page<Connection> getConnectionList(String projectId, String connectionTypeId, Pageable pageable) {
        if (projectId != null && connectionTypeId != null)
            return connectionRepository.findByProjectIdAndConnectionTypeIdAndIsDeleted(projectId, connectionTypeId,
                    false, pageable);
        if (projectId != null && !projectId.isEmpty())
            return connectionRepository.findByProjectIdAndIsDeleted(projectId,
                    false, pageable);
        throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                CustomMessages.PROJECT_ID_REQUIRED));
    }

    public Connection updateConnection(String authentication, ConnectionUpdatePayload connectionUpdatePayload) {

        /*
         *
         * -- 1. Check name of the connection. If already used in a project then
         * restrict the update.
         *
         * -- 2. Check if the connection type is a JDBC connector, if yes then update
         * the properties and save.
         *
         * -- 3. If the connection type is not a JDBC connector then check if it is a
         * re-authorization process, if yes then update the access token in the
         * connection properties.
         *
         * -- 4. If it is not a re-authorization process then simply update the name of
         * the existing connection.
         */

        Optional<Connection> connection = connectionRepository.findByIdAndAndIsDeleted(connectionUpdatePayload.getId(), false);
        if (connection.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    CustomMessages.CONNECTION_NOT_FOUND));

        ResponseEntity<Object> projectResp = adminService.getProject(connection.get().getProjectId(), authentication);
        if (projectResp.getStatusCode().isError())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    projectResp.getBody()));
        Project project = (Project) projectResp.getBody();

        connection.get().setAccountId(project.getOrganizationId());
        //        connection.setAccountName(project.get());
        connection.get().setProjectName(project.getName());
        connection.get().setProjectId(project.getId());

        if (connection.get().getName().equals(connectionUpdatePayload.getName())
                && isConnectionNameTaken(connectionUpdatePayload.getName(), connection.get().getAccountId(),
                connection.get().getProjectId(), false)) {
            throw new ApiException(new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.CONNECTION_NAME_ALREADY_TAKEN
            ));
        }

        LOG.debug("Update payload : {}", connectionUpdatePayload);

        validateName(connection.get().getName(),
                connectionUpdatePayload.getName(), connection.get().getProjectId());

        connection.get().setName(connectionUpdatePayload.getName());
        connection.get().setAccountCode(project.getOrganizationId());
        connection.get().setProjectCode(project.getProjectCode());

        metadataUtil.updateMetadata(connection.get().getMetadata(), authentication);

        Optional<ConnectionType> connectionType = connectionTypeRepository
                .findByIdAndIsDeleted(connection.get().getConnectionTypeId(), false);

        if (connectionType.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.INVALID_CONNECTION_TYPE));

        if (!connectionType.get().getConnectionTypeDetails().isConnectionTokenRequired()) {

            /*
             * Update the connection properties.
             */
            connection.get().getProperties().putAll(connectionUpdatePayload.getProperties());

            /*
             * UI will validate the connection details first before updating the connection
             * details. If the test result is successful then and only then the update
             * request is made.
             *
             * Check if the name of the connection is changed and if yes then update the
             * connection's name in flows as well.
             *
             * We will be making this connection as invalid and re-authorization as true for
             * updating the flows. This will make sure that the connection gets valid and
             * the flows that are using this connection also get updated and gets valid.
             */

            //            connection.get().setInvalid(true);

            updateConnectionFlows(true, connection.get());


            connectionRepository.save(connection.get());

            return findConnection(authentication, connection.get().getId(), connectionType.get());

        }
        if (connectionUpdatePayload.getReAuth() != null && connectionUpdatePayload.getReAuth()) {
            LOG.debug("Updating connection for re-authorization...");
            /*
             * setConnectionPropertiesFromConnectionToken method updates the connection
             * properties and requires the connection object to have the connection token
             * id. So setting here the connection token id in existing connection object.
             */
            setConnectionPropertiesFromConnectionToken(connectionUpdatePayload.getConnectionToken(), connection.get(),
                    connectionType.get());
        }

        /*
         * If the control is here then it means that this connection is not a JDBC
         * connection. And it can be a re-authorization update process. The connection
         * properties are already updated by the Re-Authorization check block. If it is
         * not a re-authorization process then only the name is changed. All the invalid
         * flows
         */
        updateConnectionFlows(connectionUpdatePayload.getReAuth(), connection.get());
        connectionRepository.save(connection.get());

        /*
         * Send an update event if the connection type requires a controller service
         * then the NiFi Encapsulator will take care of it.
         */
        return findConnection(authentication, connection.get().getId(), connectionType.get());
    }


    private Connection findConnection(String authentication, String connectionId, ConnectionType connectionType) {
        Optional<Connection> connection = connectionRepository.findByIdAndConnectionTypeIdAndIsDeleted(connectionId, connectionType.getId(), false);
        if (connection.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.CONNECTION_NOT_FOUND));

        connection.get().addProperty("displayProperties", connectionType.getConnectionTypeDetails().getDisplayProperties());
        connection.get().addProperty("provider", connectionType.getConnectionTypeDetails().getProvider());

        connection.get().setUpgradeAvailable(connection.get().getVersion() < connectionType.getVersion());

        return connection.get();

    }

    private void updateConnectionFlows(boolean isReAuth, Connection connection) {
        if (connection == null)
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.CONNECTION_NOT_FOUND));

        connection.setNormalizedName(connection.getName().toUpperCase());

        /* Updating the connection name in flows where this connection is used. */
        //        flowService.updateConnectionInFlow(connection);

        if (connection.isInvalid() && isReAuth) {
            LOG.debug("Connection is being valid again.");
            //            flowService.makeFlowsValid(connection);
            connection.setInvalid(false);
        }
    }

    private void validateName(String oldName, String newName, String projectId) {
        if (!newName.equals(oldName)) {
            Optional<Connection> connectionWithProvidedName = connectionRepository
                    .findByNameAndProjectIdAndIsDeleted(newName, projectId, false);

            if (connectionWithProvidedName.isPresent())
                throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        CustomMessages.CONNECTION_NAME_ALREADY_TAKEN));
        }
    }


    public Object getConnection(String authentication, String connectionId, String connectionTypeId) {
        Optional<ConnectionType> connectionType = connectionTypeRepository.findByIdAndIsDeleted(connectionTypeId, false);
        if (connectionType.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    CustomMessages.CONNECTION_TYPE_NOT_FOUND));

        if (connectionType.get().getStatus().equals(OperationModes.COMING_SOON.toString()))
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.INVALID_CONNECTION_TYPE));

        return findConnection(authentication, connectionId, connectionType.get());
    }

    public String testConnection(String connectionId) {

        Assert.notNull(connectionId, "Connection Id is required.");

        Optional<Connection> connection = connectionRepository.findByIdAndIsDeleted(connectionId, false);

        if (connection.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    CustomMessages.CONNECTION_NOT_FOUND));

        Optional<ConnectionType> connectionType = connectionTypeRepository.findByIdAndTypeAndIsDeleted(
                connection.get().getConnectionTypeId(),
                connection.get().getConnectionObjectType(), false);

        if (connectionType.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    CustomMessages.CONNECTION_TYPE_NOT_FOUND));

        LOG.debug("Going through the connection test execution module: {}", connectionType.get().getName());


        return invokeTestConnection(connectionType.get(), connection.get());
    }

    private String invokeTestConnection(ConnectionType connectionType, Connection connection) {

        try {
            /* Getting the bean for the connector service from bean factory. */
            Object executorInstance = beanFactory
                    .getBean(Utils.getBeanName(connectionType.getConnectionTypeDetails().getImplementingClass()));

            /*
             * Finding the method from the bean found from the connection type's executor.
             */
            Method executorMethod = executorInstance.getClass().getDeclaredMethod(Constants.TEST_CONNECTION, Connection.class,
                    ConnectionType.class);
            executorMethod.invoke(executorInstance, connection,
                    connectionType);
        } catch (IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            LOG.error(ExceptionStackTrace.getStackTrace(e));
            throw new ApiException(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    CustomMessages.SOMETHING_WENT_WRONG_TRY_LATER));
        } catch (NoSuchMethodException e) {
            LOG.error("Method not found for the connection type : {}", connectionType.getName());
            LOG.error(ExceptionStackTrace.getStackTrace(e));
            throw new ApiException(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    CustomMessages.SOMETHING_WENT_WRONG_TRY_LATER));
        } catch (SecurityException e) {
            LOG.error("Security constraint violated for connection type's method : {}",
                    connectionType.getName());
            LOG.error(ExceptionStackTrace.getStackTrace(e));
            throw new ApiException(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    CustomMessages.SOMETHING_WENT_WRONG_TRY_LATER));
        }
        return Constants.CONNECTION_SUCCESS;
    }

    public String deleteConnections(List<String> ids, String authentication) {
        List<Connection> allConnections = connectionRepository.findAllByIdInAndIsDeletedAndIsPrivate(ids, false, false);

        if (allConnections.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), CustomMessages.CONNECTION_NOT_FOUND));


        for (Connection connection : allConnections) {
            softDeleteConnection(connection, authentication);
        }

        return Constants.SUCCESSFULLY_DELETED;

    }

    private void softDeleteConnection(Connection connection, String authentication) {

        /**
         * Check here if this connection is used in any pipeline
         * if yes so throw the exception as "Connection is beign
         * used in the pipeline with name"
         */

        connection.setDeleted(true);
        metadataUtil.updateMetadata(connection.getMetadata(), authentication);
        connectionRepository.save(connection);
    }
}
