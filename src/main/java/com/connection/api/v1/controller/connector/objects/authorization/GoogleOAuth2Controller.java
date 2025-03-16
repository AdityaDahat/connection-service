package com.connection.api.v1.controller.connector.objects.authorization;

import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.service.connector.objects.authorization.GoogleOAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
                .body(new ApiResponse<>(googleOAuth2Service.getAuthorizationURL(connectionTypeId, uiCallback, connectionId, reAuth, setupMode)));
    }

    @GetMapping(path = "/callback")
    public ModelAndView getTokenAuth(@RequestParam(value = "code", required = false) String code,
                                     @RequestParam("state") String state, HttpServletRequest request) {
        return new ModelAndView(googleOAuth2Service.handleCallback(code, state, request));
    }
}
