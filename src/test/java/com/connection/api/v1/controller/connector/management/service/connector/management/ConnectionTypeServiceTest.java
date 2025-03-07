package com.connection.api.v1.controller.connector.management.service.connector.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.ConnectionTypeCategory;
import com.connection.api.v1.model.connector.management.ConnectionTypeDetails;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCreationPayload;
import com.connection.api.v1.repository.connector.management.ConnectionTypeCategoryRepository;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.service.connector.management.ConnectionTypeService;
import com.connection.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class ConnectionTypeServiceTest {

    @Mock
    private ConnectionTypeRepository connectionTypeRepository;

    @Mock
    private ConnectionTypeCategoryRepository connectionTypeCategoryRepository;

    @Mock
    private MetadataUtil metadataUtil;

    @InjectMocks
    private ConnectionTypeService connectionTypeService;

    private ConnectionTypeCreationPayload payload;
    private String authToken = "Bearer sample-token";

    @BeforeEach
    void setUp() {
        payload = new ConnectionTypeCreationPayload();
        payload.setName("Test Connection");
        payload.setShortName("TEST");
        payload.setVersion(1.0f);
        payload.setGlobalConnectionType(true);
        payload.setPrivate(false);
        payload.setSkipFlowScripts(false);

        ConnectionTypeDetails details = new ConnectionTypeDetails();
        details.setCategoryId("1");
        details.setProcessType("PROCESS_TYPE");
        payload.setConnectionTypeDetails(details);
    }

    @Test
    void createConnectionType_ShouldSaveSuccessfully_WhenValidRequest() {
        when(connectionTypeRepository.findByNameAndIsDeleted(payload.getName(), false)).thenReturn(Optional.empty());

        ConnectionTypeCategory category = new ConnectionTypeCategory();
        category.setId("1");
        category.setName("Test Category");
        when(connectionTypeCategoryRepository.findByIdAndIsDeleted("1", false)).thenReturn(Optional.of(category));

//        doNothing().when(metadataUtil).createMetadata(authToken);

        ConnectionType savedConnectionType = new ConnectionType();
        savedConnectionType.setName(payload.getName());
        when(connectionTypeRepository.save(any(ConnectionType.class))).thenReturn(savedConnectionType);

        ConnectionType result = connectionTypeService.createConnectionType(payload, authToken);

        assertNotNull(result);
        assertEquals("Test Connection", result.getName());

        verify(connectionTypeRepository, times(1)).save(any(ConnectionType.class));
        verify(metadataUtil, times(1)).createMetadata(authToken);
    }

    @Test
    void createConnectionType_ShouldThrowException_WhenConnectionTypeExists() {
        when(connectionTypeRepository.findByNameAndIsDeleted(payload.getName(), false))
                .thenReturn(Optional.of(new ConnectionType()));

        ApiException exception = assertThrows(ApiException.class, () -> {
            connectionTypeService.createConnectionType(payload, authToken);
        });

        assertTrue(exception.getMessage().contains("A connection type with the name Test Connection already exists"));

        verify(connectionTypeRepository, never()).save(any());
    }

    @Test
    void createConnectionType_ShouldThrowException_WhenCategoryNotFound() {

        when(connectionTypeRepository.findByNameAndIsDeleted(payload.getName(), false)).thenReturn(Optional.empty());
        when(connectionTypeCategoryRepository.findByIdAndIsDeleted("1", false)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            connectionTypeService.createConnectionType(payload, authToken);
        });

        assertTrue(exception.getMessage().contains("Connection type category not found"));

        verify(connectionTypeRepository, never()).save(any());
    }
}
