package com.connection.api.v1.service.connector.management;

import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionDefinition;
import com.connection.api.v1.model.enums.Modes;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.payload.connector.management.ConnectionDefinitionCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionDefinitionUpdatePayload;
import com.connection.api.v1.repository.connector.management.ConnectionDefinitionRepository;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Service
public class ConnectionDefinitionService {

    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionDefinitionRepository connectionDefinitionRepository;
    private final MetadataUtil metadataUtil;

    @Autowired
    public ConnectionDefinitionService(ConnectionTypeRepository connectionTypeRepository, ConnectionDefinitionRepository connectionDefinitionRepository,
                                       @Lazy MetadataUtil metadataUtil) {
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionDefinitionRepository = connectionDefinitionRepository;
        this.metadataUtil = metadataUtil;
    }

    public ConnectionDefinition createConnectionDefinition(ConnectionDefinitionCreationPayload payload, JwtAuthenticationToken authentication) {
        if (connectionTypeRepository.findByIdAndIsDeleted(payload.getConnectionTypeId(), false).isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_NOT_FOUND));

        if (connectionDefinitionRepository.findByNameAndModeAndIsDeleted(payload.getName(),
                payload.getMode(), false).isPresent())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value()
                    , "Connection-definition with the name '" + payload.getName()
                    + "' already exist in '" + payload.getMode() + "' mode."));

        return saveConnectionDefinition(payload, authentication);
    }

    private ConnectionDefinition saveConnectionDefinition(ConnectionDefinitionCreationPayload payload, JwtAuthenticationToken authentication) {
        ConnectionDefinition connectionDefinition = new ConnectionDefinition();
        connectionDefinition.setAttributes(payload.getAttributes());
        connectionDefinition.setConnectionTypeId(payload.getConnectionTypeId());
        connectionDefinition.setConnectionTypeName(payload.getConnectionTypeName());
        connectionDefinition.setDisplayName(payload.getDisplayName());
        connectionDefinition.setName(payload.getName());
        connectionDefinition.setOther(payload.getOther());
        connectionDefinition.setVersion(payload.getVersion());
        connectionDefinition.setMetadata(metadataUtil.createMetadata(authentication.getToken().getTokenValue()));
        connectionDefinition.setMode(Modes.PREVIEW.getMode());

        return connectionDefinitionRepository.save(connectionDefinition);

    }

    public ConnectionDefinition updateConnectionDefinition(ConnectionDefinitionUpdatePayload payload, JwtAuthenticationToken authentication) {
        Optional<ConnectionDefinition> connectionDefinition = connectionDefinitionRepository.findByIdAndIsDeleted(payload.getId(), false);
        if (connectionDefinition.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_DEFINITION_NOT_FOUND));

        if (connectionTypeRepository.findByIdAndIsDeleted(payload.getConnectionTypeId(), false).isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_NOT_FOUND));

        Modes mode = Modes.parse(payload.getMode());

        if (mode == null)
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Value of mode is invalid."));

        if (!mode.getMode().equals(connectionDefinition.get().getMode())) {
            Optional<ConnectionDefinition> anotherDefinition = connectionDefinitionRepository
                    .findByIdAndConnectionTypeIdAndModeAndNameAndIsDeleted(payload.getId(),
                            payload.getConnectionTypeId(), mode.getMode(), payload.getName(), false);

            if (anotherDefinition.isPresent())
                throw new ApiException(new ApiResponse<>(HttpStatus.CONFLICT.value(),
                        ErrorMessage.STABLE_SOURCE_DEFINITION_COULD_NOT_BE_UPDATED));

        }

        return updateConnectionDefinition(connectionDefinition.get(), payload, mode, authentication);
    }

    private ConnectionDefinition updateConnectionDefinition(ConnectionDefinition connectionDefinition, ConnectionDefinitionUpdatePayload payload, Modes mode, JwtAuthenticationToken authentication) {
        connectionDefinition.setAttributes(payload.getAttributes());
        connectionDefinition.setDisplayName(payload.getDisplayName());
        connectionDefinition.setName(payload.getName());
        connectionDefinition.setConnectionTypeName(payload.getConnectionTypeName());
        connectionDefinition.setObjectType(payload.getObjectType());
        connectionDefinition.setOther(payload.getOther());
        connectionDefinition.setVersion(payload.getVersion());
        connectionDefinition.setMode(mode.getMode());
        connectionDefinition.setMetadata(metadataUtil.updateMetadata(connectionDefinition.getMetadata(), authentication.getToken().getTokenValue()));
        return connectionDefinitionRepository.save(connectionDefinition);
    }

    public Page<ConnectionDefinition> listConnectionDefinition(Pageable pageable, String connectionTypeId) {
        if (connectionTypeId != null && !connectionTypeId.isEmpty())
            return connectionDefinitionRepository.findAllByConnectionTypeIdAndIsDeleted(connectionTypeId, false, pageable);

        return connectionDefinitionRepository.findAllByIsDeleted(false, pageable);
    }

    public Object getConnectionDefinitionInfo(String connectionDefinitionId) {
        Optional<ConnectionDefinition> connectionDefinition = connectionDefinitionRepository.findByIdAndObjectTypeInAndIsDeleted(
                connectionDefinitionId,
                Arrays.asList(ConnectionDefinition.SOURCE_DEFINITION, ConnectionDefinition.TARGET_DEFINITION),
                false);
        if (connectionDefinition.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_DEFINITION_NOT_FOUND));
        return connectionDefinition.get();
    }

    public List<ConnectionDefinition> getConnectionDefinitionDetails(String connectionDefName, String connectionTypeId,
                                                                     String objectType) {
        List<ConnectionDefinition> connectionDefDetails = connectionDefinitionRepository
                .findByNameContainingIgnoreCaseAndConnectionTypeIdAndObjectType(connectionDefName, connectionTypeId, objectType);
        if (connectionDefDetails != null && !connectionDefDetails.isEmpty())
            return connectionDefDetails;
        throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                ErrorMessage.CONNECTION_DEFINITION_NOT_FOUND));
    }

    public String deleteConnectionDefinitions(List<String> ids, JwtAuthenticationToken authentication) {
        List<ConnectionDefinition> connectionDefinitions = connectionDefinitionRepository.findAllById(ids);
        if (!connectionDefinitions.isEmpty()) {

            if (connectionDefinitions.size() == ids.size()) {
                softDeleteConnectionDefinitions(connectionDefinitions, authentication);
                return CustomMessages.CONNECTION_DEFINITIONS_DELETED;
            } else if (connectionDefinitions.size() < ids.size()) {
                softDeleteConnectionDefinitions(connectionDefinitions, authentication);
                return CustomMessages.SOME_CONNECTION_DEFINITIONS_DELETED;
            }
        }
        throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                ErrorMessage.CONNECTION_DEFINITION_NOT_FOUND));
    }

    private void softDeleteConnectionDefinitions(List<ConnectionDefinition> connectionDefinitions, JwtAuthenticationToken authentication) {
        ListIterator<ConnectionDefinition> i = connectionDefinitions.listIterator();
        String authToken = authentication.getToken().getTokenValue();
        ConnectionDefinition temp;
        while (i.hasNext()) {
            temp = i.next();
            metadataUtil.updateMetadata(temp.getMetadata(), authToken);
            temp.setDeleted(true);
            i.set(temp);
        }
        connectionDefinitionRepository.saveAll(connectionDefinitions);
    }
}
