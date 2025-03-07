package com.connection.api.v1.controller.connector.management;

import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.payload.connector.management.ConnectionDefinitionCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionDefinitionUpdatePayload;
import com.connection.api.v1.service.connector.management.ConnectionDefinitionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/management")
public class ConnectionDefinitionController {

    private final ConnectionDefinitionService connectionDefinitionService;

    @Autowired
    public ConnectionDefinitionController(ConnectionDefinitionService connectionDefinitionService) {
        this.connectionDefinitionService = connectionDefinitionService;
    }

    @PostMapping("/connection-definition")
    public ResponseEntity<Object> createConnectionDefinition(
            @Validated @RequestBody ConnectionDefinitionCreationPayload creationPayload,
            JwtAuthenticationToken authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionDefinitionService.createConnectionDefinition(creationPayload, authentication));
    }

    @PutMapping("/connection-definition")
    public ResponseEntity<Object> updateConnectionDefinition(
            @Validated @RequestBody ConnectionDefinitionUpdatePayload connectionDefinitionUpdatePayload,
            JwtAuthenticationToken jwtAuthenticationToken) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionDefinitionService.updateConnectionDefinition(connectionDefinitionUpdatePayload, jwtAuthenticationToken));
    }

    @GetMapping("/connection-definitions")
    public ResponseEntity<Object> listConnectionDefinition(@ParameterObject Pageable pageable,
                                                           @RequestParam(value = "connectionTypeId", required = false) String connectionTypeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(connectionDefinitionService.listConnectionDefinition(pageable, connectionTypeId)));
    }

    @GetMapping("/connection-definition/{id}")
    public ResponseEntity<Object> connectionDefinitionInfo(@PathVariable("id") String connectionDefinitionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionDefinitionService.getConnectionDefinitionInfo(connectionDefinitionId));
    }

    @GetMapping("/search/connection-definitions")
    public ResponseEntity<Object> getConnectionDefinitionDetails(
            @RequestParam(value = "search", required = true) String searchName,
            @RequestParam(value = "connection-type-id", required = false) String connectionTypeId,
            @RequestParam(value = "object-type", required = false) String objectType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionDefinitionService.getConnectionDefinitionDetails(searchName, connectionTypeId, objectType));
    }

    @DeleteMapping("/connection-definition")
    public ResponseEntity<Object> deleteConnectionDefinitions(@RequestBody List<String> ids,
                                                              JwtAuthenticationToken authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionDefinitionService.deleteConnectionDefinitions(ids, authentication));
    }
}
