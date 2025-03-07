package com.connection.api.v1.service.connector.objects;

import com.connection.api.v1.model.connector.objects.Connection;
import com.connection.api.v1.payload.connector.objects.ConnectionSetupPayload;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    public Object createConnection(ConnectionSetupPayload payload, JwtAuthenticationToken authentication) {

        Connection connection = new Connection();
        connection.setName(payload.getName());
        connection.setProjectId(payload.getProjectId());
        connection.setProperties(payload.getProperties());
        connection.setCustomAttributes(payload.getCustomAttributes());

//        Project project = projectService.getProject(connection.getProjectId(), authentication);

        return null;

    }
}
