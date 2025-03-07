package com.connection.api.v1.service.connector.management;

import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.ConnectionTypeCategory;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCategoryCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCategoryUpdatePayload;
import com.connection.api.v1.repository.connector.management.ConnectionTypeCategoryRepository;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Service
public class ConnectionTypeCategoryService {

    private final ConnectionTypeCategoryRepository connectionTypeCategoryRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final MetadataUtil metadataUtil;

    @Autowired
    public ConnectionTypeCategoryService(ConnectionTypeCategoryRepository connectionTypeCategoryRepository,
                                         ConnectionTypeRepository connectionTypeRepository,
                                         MetadataUtil metadataUtil) {
        this.connectionTypeCategoryRepository = connectionTypeCategoryRepository;
        this.connectionTypeRepository = connectionTypeRepository;
        this.metadataUtil = metadataUtil;
    }

    public ConnectionTypeCategory createConnectionTypeCategory(ConnectionTypeCategoryCreationPayload payload, String authenticationToken) {

        Optional<ConnectionTypeCategory> connectionTypeCategory = connectionTypeCategoryRepository.findByIdAndIsDeleted(payload.getName(),
                false);

        if (connectionTypeCategory.isPresent()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_CATEGORY_NAME_TAKEN));
        }
        return createCategory(payload, authenticationToken);
    }

    private ConnectionTypeCategory createCategory(ConnectionTypeCategoryCreationPayload payload, String authenticationToken) {
        ConnectionTypeCategory newCategory = new ConnectionTypeCategory();
        newCategory.setName(payload.getName());
        newCategory.setType((!payload.getType().equalsIgnoreCase(ConnectionTypeCategory.SOURCE_CATEGORY)
                ? ConnectionTypeCategory.TARGET_CATEGORY
                : ConnectionTypeCategory.SOURCE_CATEGORY));
        newCategory.setAdditionalInfo(payload.getAdditionalInfo());
        newCategory.setMetadata(metadataUtil.createMetadata(authenticationToken));

        return connectionTypeCategoryRepository.save(newCategory);
    }

    public Object updateConnectionTypeCategory(ConnectionTypeCategoryUpdatePayload payload, JwtAuthenticationToken authenticationToken) {
        Optional<ConnectionTypeCategory> category = connectionTypeCategoryRepository
                .findByIdAndIsDeleted(payload.getId(), false);

        if (category.isEmpty()) {
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_CATEGORY_NAME_TAKEN));
        }

        if (payload.getName() != null && !payload.getName().isBlank()) {
            category.get().setName(payload.getName());
        }

        category.get()
                .setType((!payload.getType().equalsIgnoreCase(ConnectionTypeCategory.SOURCE_CATEGORY)
                        ? ConnectionTypeCategory.TARGET_CATEGORY
                        : ConnectionTypeCategory.SOURCE_CATEGORY));
        category.get().getAdditionalInfo().putAll(payload.getAdditionalInfo());
        metadataUtil.updateMetadata(category.get().getMetadata(), authenticationToken.getToken().getTokenValue());

        if (!category.get().getName().equals(payload.getName())) {
            updateAllConnectionTypes(category.get().getId(), category.get().getName());
        }

        return connectionTypeCategoryRepository.save(category.get());

    }

    private void updateAllConnectionTypes(String connectionTypeCategoryId, String name) {
        List<ConnectionType> allConnectionType = connectionTypeRepository.findByIsDeleted(false);

        List<ConnectionType> connectionTypes = allConnectionType.stream()
                .filter(ct -> ct.getConnectionTypeDetails() != null
                        && connectionTypeCategoryId.equals(ct.getConnectionTypeDetails().getCategoryId()))
                .toList();

        ListIterator<ConnectionType> i = connectionTypes.listIterator();
        ConnectionType connectionType = null;

        while (i.hasNext()) {
            connectionType = i.next();
            connectionType.getConnectionTypeDetails().setCategory(name);
            i.set(connectionType);
        }
        connectionTypeRepository.saveAll(connectionTypes);
    }

    public Page<ConnectionTypeCategory> getConnectionTypeCategories(String search, String type, Pageable pageable) {
        boolean hasSearch = search != null && !search.isBlank();
        boolean hasType = type != null && !type.isBlank();

        if (hasSearch && hasType)
            return connectionTypeCategoryRepository.findByNameContainingIgnoreCaseAndTypeAndIsDeleted(search, type, false, pageable);
        if (hasSearch)
            return connectionTypeCategoryRepository.findByNameContainingIgnoreCaseAndIsDeleted(search, false, pageable);
        if (hasType)
            return connectionTypeCategoryRepository.findByTypeAndIsDeleted(type, false, pageable);
        return connectionTypeCategoryRepository.findByIsDeleted(false, pageable);
    }

    public ConnectionTypeCategory deleteConnectionTypeCategory(String categoryId, JwtAuthenticationToken authentication) {
        Optional<ConnectionTypeCategory> connectionTypeCategory = connectionTypeCategoryRepository.findByIdAndIsDeleted(categoryId, false);

        if (connectionTypeCategory.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_CATEGORY_NOT_FOUND));

        connectionTypeCategory.get().setDeleted(true);
        metadataUtil.updateMetadata(connectionTypeCategory.get().getMetadata(), authentication.getToken().getTokenValue());
        return connectionTypeCategoryRepository.save(connectionTypeCategory.get());
    }

    public ConnectionTypeCategory getConnectionTypeCategory(String categoryId) {
        Optional<ConnectionTypeCategory> connectionTypeCategory = connectionTypeCategoryRepository.findByIdAndIsDeleted(categoryId, false);
        if (connectionTypeCategory.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_CATEGORY_NOT_FOUND));
        return connectionTypeCategory.get();
    }
}
