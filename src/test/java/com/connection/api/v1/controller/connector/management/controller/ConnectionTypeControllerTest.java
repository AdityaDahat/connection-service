package com.connection.api.v1.controller.connector.management.controller;

import com.connection.api.v1.controller.connector.management.ConnectionTypeController;
import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.ConnectionTypeDetails;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCreationPayload;
import com.connection.api.v1.service.connector.management.ConnectionTypeService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ConnectionTypeControllerTest {

    @InjectMocks
    private ConnectionTypeController connectionTypeController;

    @Mock
    private ConnectionTypeService connectionTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createConnectionType_ShouldReturnCreatedStatus() {
        // Arrange: Create payload
        ConnectionTypeCreationPayload payload = new ConnectionTypeCreationPayload();
        payload.setName("Test Connection");
        payload.setShortName("TC");
        payload.setConnectionTypeDetails(new ConnectionTypeDetails());

        // Mock service response
        ConnectionType mockConnectionType = new ConnectionType();
        mockConnectionType.setName("Test Connection");

        when(connectionTypeService.createConnectionType(any(), anyString()))
                .thenReturn(mockConnectionType);

        // Act: Call the controller method
        ResponseEntity<Object> response = connectionTypeController.createConnectionType(payload, "Bearer test-token");

        // Assert: Check response status and data
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
