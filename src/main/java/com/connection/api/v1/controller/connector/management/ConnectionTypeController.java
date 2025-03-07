package com.connection.api.v1.controller.connector.management;

import com.connection.api.v1.payload.connector.management.ConnectionTypeCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionTypeUpdatePayload;
import com.connection.api.v1.service.connector.management.ConnectionTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/management")
public class ConnectionTypeController {
    @Autowired
    private final ConnectionTypeService connectionTypeService;

    public ConnectionTypeController(ConnectionTypeService connectionTypeService) {
        this.connectionTypeService = connectionTypeService;
    }

    @PostMapping("/connection-type")
    public ResponseEntity<Object> createConnectionType(
            @Validated @RequestBody ConnectionTypeCreationPayload connectionTypeCreationPayload,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionTypeService.createConnectionType(connectionTypeCreationPayload , authorization));
    }

    @PutMapping("/connection-type")
    public ResponseEntity<Object> updateConnectionType(
            @Validated @RequestBody ConnectionTypeUpdatePayload connectionTypeCreationPayload) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionTypeService.updateConnectionType(connectionTypeCreationPayload));
    }

    @GetMapping("/connection-types")
    public ResponseEntity<Object> getConnectionTypes(
            @RequestParam(value = "connection-object-type", required = false) String connectionObjectType,
            @RequestParam(value = "source-connection-type-id", required = false) String sourceConnectionTypeId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(connectionTypeService.getConnectionTypes(connectionObjectType, sourceConnectionTypeId, pageable));
    }

    @GetMapping("/connection-type/{id}")
    public ResponseEntity<Object> getConnectionType(@PathVariable("id") String id,
                                                    @RequestParam(value = "include-client-credentials", required = false) boolean includeClientCredentials) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(connectionTypeService.getConnectionType(id));
    }

    @DeleteMapping("/connection-type")
    public ResponseEntity<Object> deleteConnectionType(@RequestBody List<String> ids) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(connectionTypeService.deleteConnectionType(ids));
    }

    @GetMapping("/search/connection-type")
    public ResponseEntity<Object> getConnectionTypeByName(@RequestParam("name") String name,
                                                          @ParameterObject Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(connectionTypeService.getConnectionTypeByName(name, pageable));
    }

}
