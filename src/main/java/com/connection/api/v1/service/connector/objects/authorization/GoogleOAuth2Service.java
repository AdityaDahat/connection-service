package com.connection.api.v1.service.connector.objects.authorization;

import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.objects.ConnectionToken;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionTokenRepository;
import com.connection.constants.Connectors;
import com.connection.constants.Constants;
import com.connection.exception.ApiException;
import com.connection.security.ApplicationProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoogleOAuth2Service {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleOAuth2Service.class);
    public static final String DEVELOPER_TOKEN = "F4JTbjxOFYXs1SHM_F5Rpw";
    public static final String CONNECTION_TYPE_ID = "5c52fac365e39e0e5c238db7";
    public static final String CLIENT_ID = "537816608564-309ehta7slv9lace18f8pbmldsrbmd1e.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "a2B3IcCsJdkc7cbhyBYdD4WT";
    public static final String REDIRECT_URI = "/v1/connector/google/oauth2/callback";
    public static final String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    private static final String REVOKE_URL = "https://accounts.google.com/o/oauth2/revoke";
    public static final String GOOGLE_TOKEN_INFO = "googleTokenInfo";
    private static final String ERROR = "error=";
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String UNAUTHORIZED = "401 Unauthorized";
    private static final String FORBIDDEN = "403 Forbidden";
    private static final String SETUP_MODE = "setupMode";

    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionTokenRepository connectionTokenRepository;
    private final ConnectionRepository connectionRepository;

    private final ObjectMapper objectMapper;

    private final ApplicationProperties applicationProperties;

    @Autowired
    public GoogleOAuth2Service(ConnectionTypeRepository connectionTypeRepository,
                               ConnectionTokenRepository connectionTokenRepository,
                               ConnectionRepository connectionRepository,
                               ObjectMapper objectMapper,
                               ApplicationProperties applicationProperties) {
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionTokenRepository = connectionTokenRepository;
        this.connectionRepository = connectionRepository;
        this.objectMapper = objectMapper;
        this.applicationProperties = applicationProperties;
    }

    public String getAuthorizationURL(String connectionTypeId, String uiCallback, String connectionId, boolean reAuth, String setupMode) {

        ConnectionType connectionType = validateRequestForAuthorizationUrl(connectionTypeId, connectionId, reAuth);

        GoogleAuthorizationCodeFlow authorizationCodeFlow;
        authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, GSON_FACTORY, CLIENT_ID,
                CLIENT_SECRET, resolveScopes(setupMode, connectionType)).setApprovalPrompt("force")
                .setAccessType("offline").build();

        //THis needs to be corrected
        String url = authorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri(applicationProperties.getSecurity().getApiMatcher() + applicationProperties.getSecurity().getAbstractPath() + REDIRECT_URI).build();

        //        actual is
        //String url = authorizationFlow.newAuthorizationUrl()
                //.setRedirectUri(configurations.getApiDomain() + configurations.getContextPath() + REDIRECT_URI).build();
        if (uiCallback == null || uiCallback.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.UI_CALLBACK_REQUIRED));

        JsonObject state = new JsonObject();
        state.addProperty(Constants.STATE, connectionTypeId);
        state.addProperty(Connectors.Authorization.UI_CALLBACK, uiCallback);
        state.addProperty(Connectors.Authorization.RE_AUTH, reAuth);
        state.addProperty(Connectors.Authorization.CONNECTION_ID, connectionId);

        if (setupMode != null)
            state.addProperty(SETUP_MODE, setupMode);

        url = url + "&state=dataByte(" + Base64.getEncoder().encodeToString(state.toString().getBytes()) + ")";
        return url;
    }


    private Collection<String> resolveScopes(String setupMode, ConnectionType connectionType) {

        if (connectionType.getId().equalsIgnoreCase("5c52fac365e39e0e5c238db7")) {

            if (setupMode != null && setupMode.equalsIgnoreCase("with-gtag")) {
                return connectionType.getConnectionTypeDetails().getScopes();
            } else {

                if (connectionType.getConnectionTypeDetails().getAdditionalInfo().containsKey("tag_manager_scopes")) {

                    List<String> gtmScopes = objectMapper.convertValue(
                            connectionType.getConnectionTypeDetails().getAdditionalInfo().get("tag_manager_scopes"),
                            new TypeReference<List<String>>() {
                            });

                    ArrayList<String> scopes = new ArrayList<>();
                    scopes.addAll(gtmScopes);
                    scopes.addAll(connectionType.getConnectionTypeDetails().getScopes());
                    return scopes;
                } else {
                    return connectionType.getConnectionTypeDetails().getScopes();
                }
            }
        }

        return connectionType.getConnectionTypeDetails().getScopes();
    }

    private ConnectionType validateRequestForAuthorizationUrl(String connectionTypeId, String connectionId, boolean reAuth) {
        Optional<ConnectionType> connectionType = connectionTypeRepository.findByIdAndIsDeleted(connectionTypeId, false);
        if (connectionType.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TYPE_NOT_FOUND));

        if (!connectionType.get().getConnectionTypeDetails().getProvider().equalsIgnoreCase(Constants.GOOGLE))
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.INVALID_CONNECTION_TYPE));

        if (reAuth && connectionRepository.findByIdAndIsDeleted(connectionId, false).isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_NOT_FOUND));

        return connectionType.get();
    }

    public Optional<ConnectionToken> getConnectionToken(String connectionTokenId, String connectionTypeId) {
        return connectionTokenRepository.findByIdAndConnectionTypeId(connectionTokenId, connectionTypeId);

    }
}
