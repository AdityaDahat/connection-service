package com.connection.api.v1.controller.connector.management;

import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCategoryCreationPayload;
import com.connection.api.v1.payload.connector.management.ConnectionTypeCategoryUpdatePayload;
import com.connection.api.v1.service.connector.management.ConnectionTypeCategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/management")
@CrossOrigin("*")
public class ConnectionTypeCategoryController {
    private final ConnectionTypeCategoryService connectionTypeCategoryService;

    @Autowired
    public ConnectionTypeCategoryController(ConnectionTypeCategoryService connectionTypeCategoryService) {
        this.connectionTypeCategoryService = connectionTypeCategoryService;
    }

    @PostMapping("/connection-type-category")
    public ResponseEntity<Object> create(@RequestBody ConnectionTypeCategoryCreationPayload connectionTypeCategoryCreationPayload,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionTypeCategoryService.createConnectionTypeCategory(connectionTypeCategoryCreationPayload, authenticationToken));
    }

    @PutMapping("/connection-type-category")
    public ResponseEntity<Object> update(@RequestBody ConnectionTypeCategoryUpdatePayload connectionTypeCategoryUpdatePayload,
                                         JwtAuthenticationToken authenticationToken) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionTypeCategoryService.updateConnectionTypeCategory(connectionTypeCategoryUpdatePayload, authenticationToken));
    }

    @GetMapping("/connection-type-categories")
    public ResponseEntity<Object> getConnectionTypeCategories(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "type", required = false) String type, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionTypeCategoryService.getConnectionTypeCategories(search, type, pageable));
    }

    @GetMapping("/connection-type-category/{category-id}")
    public ResponseEntity<Object> getConnectionCategory(@PathVariable("category-id") String categoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionTypeCategoryService.getConnectionTypeCategory(categoryId));
    }

    @DeleteMapping("/connection-type-category/{category-id}")
    public ResponseEntity<Object> deleteConnectionTypeCategory(JwtAuthenticationToken authentication,
                                                               @PathVariable("category-id") String categoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(connectionTypeCategoryService.deleteConnectionTypeCategory(categoryId, authentication));
    }

}
