package com.connection.api.v1.controller.connector.objects.authorization;

import com.connection.api.v1.service.connector.objects.authorization.GoogleOAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/connector/google/oauth2")
public class GoogleOAuth2Controller {
    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;

    @GetMapping("/auth/{connection-type-id}")
    public ResponseEntity<Object> getAuthorizationURL(@PathVariable("connection-type-id") String connectionTypeId,
                                                      @RequestParam(value = "ui-callback") String uiCallback,
                                                      @RequestParam(value = "re-auth", required = false) boolean reAuth,
                                                      @RequestParam(value = "connection-id", required = false) String connectionId,
                                                      @RequestParam(value = "setup-mode", required = false) String setupMode) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(googleOAuth2Service.getAuthorizationURL(connectionTypeId, uiCallback, connectionId, reAuth, setupMode));
    }
}
