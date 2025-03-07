package com.connection.api.v1.service.connector.management;

import com.connection.api.v1.feign.admin.AdminService;
import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.ConnectionTypeCategory;
import com.connection.api.v1.model.connector.management.OperationModes;
import com.connection.api.v1.model.connector.management.dto.ConnectionTypeProjection;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionTypeUpdatePayload;
import com.connection.api.v1.repository.connector.management.ConnectionTypeCategoryRepository;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.constants.Connectors;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Service
public class ConnectionTypeService {
    private final ConnectionTypeCategoryRepository connectionTypeCategoryRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    @Autowired
    private AdminService adminService;
    private final MetadataUtil metadataUtil;

    public ConnectionTypeService(ConnectionTypeCategoryRepository connectionTypeCategoryRepository,
                                 ConnectionTypeRepository connectionTypeRepository,
                                 MetadataUtil metadataUtil) {
        this.connectionTypeCategoryRepository = connectionTypeCategoryRepository;
        this.connectionTypeRepository = connectionTypeRepository;
        this.metadataUtil = metadataUtil;
    }

    public ConnectionType createConnectionType(ConnectionTypeCreationPayload payload, String authToken) {

//        ResponseEntity<Object> allOrganization = adminService.getAllProjects("xyz");
//        if(allOrganization.getStatusCode().isError())
//        {
//            System.out.println(allOrganization);
//        }
//        System.out.println(allOrganization);
        Optional<ConnectionType> existingConnectionType = connectionTypeRepository.findByNameAndIsDeleted(
                payload.getName(), false);

        if (existingConnectionType.isPresent()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "A connection type with the name " + payload.getName() + " already exists"));
        }

        Optional<ConnectionTypeCategory> category = connectionTypeCategoryRepository.findByIdAndIsDeleted(
                payload.getConnectionTypeDetails().getCategoryId(), false
        );

        if (category.isEmpty()) {
            throw new ApiException(new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Connection type category not found"
            ));
        }

        ConnectionType connectionType = new ConnectionType();
        connectionType.setConnectionTypeDetails(payload.getConnectionTypeDetails());
        connectionType.getConnectionTypeDetails().setCategory(category.get().getName());
        connectionType.getConnectionTypeDetails().setCategoryId(category.get().getId());
        connectionType.getConnectionTypeDetails().setProcessType(payload.getConnectionTypeDetails().getProcessType());
        connectionType.setGlobalConnectionType(payload.isGlobalConnectionType());
        connectionType.setName(payload.getName());
        connectionType.setPrivate(payload.isPrivate());
        connectionType.setShortName(payload.getShortName());
        connectionType.setStatus(OperationModes.PREVIEW.toString());
        connectionType.setVersion(payload.getVersion());
        connectionType.setSkipFlowScripts(payload.isSkipFlowScripts());
        metadataUtil.createMetadata(authToken);
//        connectionType.setShortCode(generateShortCode(payload));
        return connectionTypeRepository.save(connectionType);
    }

    public ConnectionType updateConnectionType(ConnectionTypeUpdatePayload payload) {
        Optional<ConnectionType> existingConnectionType = connectionTypeRepository.findByNameAndIsDeleted(
                payload.getName(), false);

        if (!existingConnectionType.isPresent()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Connection Type Not Found"));
        }
        ConnectionType connectionType = existingConnectionType.get();

        if (!connectionType.getName().equals(payload.getName())) {

        }

        Optional<ConnectionTypeCategory> category = connectionTypeCategoryRepository.findByIdAndIsDeleted(
                payload.getTypeDetails().getCategoryId(), false);

        if (category.isEmpty()) {
            throw new ApiException(new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Connection type category not found"
            ));
        }
        connectionType.setConnectionTypeDetails(payload.getTypeDetails());
        connectionType.getConnectionTypeDetails().setCategory(category.get().getName());
        connectionType.getConnectionTypeDetails().setCategoryId(category.get().getId());
        connectionType.getConnectionTypeDetails().setProcessType(payload.getTypeDetails().getProcessType());
        connectionType.setGlobalConnectionType(payload.isGlobalConnectionType());
        connectionType.setName(payload.getName());
        connectionType.setPrivate(payload.isPrivate());
        connectionType.setShortName(payload.getShortName());
        connectionType.setVersion(payload.getVersion());
        connectionType.setSkipFlowScripts(payload.isSkipFlowScripts());
//        MetadataUtil.newInstance().updateMetadata(connectionType.get().getMetadata(), authentication);

        return connectionTypeRepository.save(connectionType);
    }

    public Page<ConnectionType> getConnectionTypes(String connectionObjectType, String sourceConnectionTypeId, Pageable pageable) {
        if (Connectors.SOURCE_CONNECTION.equalsIgnoreCase(connectionObjectType)) {
            return connectionTypeRepository.findAllByIsDeletedAndType(false, connectionObjectType, pageable);
        }
        return connectionTypeRepository.findAllByIsDeleted(false, pageable);
    }

    public Object deleteConnectionType(List<String> ids) {

        List<ConnectionType> connectionTypes = connectionTypeRepository.findAllByIdInAndIsDeleted(
                ids, false
        );
        if (connectionTypes.isEmpty()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_NOT_FOUND));
        }

        ListIterator<ConnectionType> i = connectionTypes.listIterator();
        ConnectionType temp;

        while (i.hasNext()) {
            temp = i.next();
//            MetadataUtil.newInstance().updateMetadata(temp.getMetadata(), authentication);
            temp.setDeleted(true);
            i.set(temp);
        }

        try {
            connectionTypeRepository.saveAll(connectionTypes);
            return CustomMessages.CONNECTION_TYPE_DELETED_SUCCESS;
        } catch (Exception e) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage()));
        }
    }

    public Page<ConnectionTypeProjection> getConnectionTypeByName(String name, Pageable pageable) {
        try {
            return connectionTypeRepository.findByNameContainingIgnoreCase(name, pageable);
        } catch (Exception e) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage()));
        }

    }

    public ConnectionType getConnectionType(String id) {
        Optional<ConnectionType> connectionType = connectionTypeRepository.findById(id);
        if (connectionType.isEmpty()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_NOT_FOUND));
        }
        return connectionType.get();
    }
}
