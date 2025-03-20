package com.connection.api.v1.controller.connector.objects;

import com.connection.api.v1.payload.connector.objects.ConnectionSetupPayload;
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

    @GetMapping("/connections")
    public ResponseEntity<Object> getConnectionList(
            @RequestParam(value = "projectId") String projectId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(
                connectionService.getConnectionList(projectId, pageable));
    }
}
