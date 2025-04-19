package com.connection.api.v1.controller.connector.objects;

import com.connection.api.v1.payload.connector.objects.ConnectionSetupPayload;
import com.connection.api.v1.payload.connector.objects.ConnectionUpdatePayload;
import com.connection.api.v1.service.connector.objects.ConnectionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/connection")
    public ResponseEntity<Object> createConnection(
            @Validated @RequestBody ConnectionSetupPayload connectionSetupPayload,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionService.createConnection(connectionSetupPayload, authentication));
    }

    @PutMapping("/connection")
    public ResponseEntity<Object> updateConnection(
            @Validated @RequestBody ConnectionUpdatePayload connectionUpdatePayload,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionService.updateConnection(authentication, connectionUpdatePayload));
    }

    @GetMapping("/connections")
    public ResponseEntity<Object> getConnectionList(
            @RequestParam(value = "projectId") String projectId,
            @RequestParam(value = "connectionTypeId", required = false) String connectionTypeId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(
                connectionService.getConnectionList(projectId,connectionTypeId, pageable));
    }

    @GetMapping("/connection/{connection-type-id}/{id}")
    public ResponseEntity<Object> getConnection(@PathVariable("connection-type-id") String connectionTypeId,
                                                @PathVariable("id") String connectionId,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionService.getConnection(authentication, connectionId, connectionTypeId));
    }

    @GetMapping("/test/connection/{connection-id}")
    public ResponseEntity<Object> testConnection(@PathVariable("connection-id") String connectionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionService.testConnection(connectionId));
    }


    @DeleteMapping("/connection")
    public ResponseEntity<Object> deleteConnections(@RequestBody List<String> ids,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authentication)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionService.deleteConnections(ids, authentication));
    }
}
