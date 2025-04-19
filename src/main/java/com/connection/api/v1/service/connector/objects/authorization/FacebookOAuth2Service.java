package com.connection.api.v1.service.connector.objects.authorization;

import org.springframework.stereotype.Service;

@Service
public class FacebookOAuth2Service {

    private static final String CLIENT_ID = "1318887995829358";
    private static final String CLIENT_SECRET = "b1a3fce9d83dfdd1296a649d7a91091b";

    public Object getAuthorizationURL(String connectionTypeId, String uiCallback, String connectionId, boolean reAuth, String setupMode) {
        return null;
    }
}
