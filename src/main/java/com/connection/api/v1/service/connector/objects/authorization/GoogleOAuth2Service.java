package com.connection.api.v1.service.connector.objects.authorization;

import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.objects.Connection;
import com.connection.api.v1.model.connector.objects.ConnectionToken;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionTokenRepository;
import com.connection.api.v1.service.connector.management.ConnectionTypeService;
import com.connection.constants.Connectors;
import com.connection.constants.Constants;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import com.connection.exception.ExceptionStackTrace;
import com.connection.security.ApplicationProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoogleOAuth2Service {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleOAuth2Service.class);
    public static final String DEVELOPER_TOKEN = "F4JTbjxOFYXs1SHM_F5Rpw";
    public static final String CONNECTION_TYPE_ID = "7bhm459fbi4avvrnteo8tmoefgpmif2ujbju";
    public static final String CLIENT_ID = "8070736070-231qq8r0ipb09e7tqlub4f6284j98qvj.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "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";
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
    private final ConnectionTypeService connectionTypeService;
    private final ApplicationProperties configurations;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GoogleOAuth2Service(ConnectionTypeRepository connectionTypeRepository,
                               ConnectionTokenRepository connectionTokenRepository,
                               ConnectionRepository connectionRepository,
                               ConnectionTypeService connectionTypeService,
                               ObjectMapper objectMapper,
                               ApplicationProperties configurations) {
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionTokenRepository = connectionTokenRepository;
        this.connectionRepository = connectionRepository;
        this.connectionTypeService = connectionTypeService;
        this.objectMapper = objectMapper;
        this.configurations = configurations;
    }

    public String getAuthorizationURL(String connectionTypeId, String uiCallback, String connectionId, boolean reAuth, String setupMode) {

        ConnectionType connectionType = validateRequestForAuthorizationUrl(connectionTypeId, connectionId, reAuth);

        GoogleAuthorizationCodeFlow authorizationCodeFlow;
        authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, GSON_FACTORY, CLIENT_ID,
                CLIENT_SECRET, resolveScopes(setupMode, connectionType)).setApprovalPrompt("force")
                .setAccessType("offline").build();

        //THis needs to be corrected
        String url = authorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8082" + "/connection" + REDIRECT_URI).build();

        LOG.info("URL {} ",url);
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
        LOG.info("URL {} ",url);
        return url;
    }

    private Collection<String> resolveScopes(String setupMode, ConnectionType connectionType) {

        if (connectionType.getId().equalsIgnoreCase(CONNECTION_TYPE_ID)) {

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

    public RedirectView handleCallback(String code, String state, HttpServletRequest request) {
        LOG.debug("request param : {}", request.getParameterMap());

        JsonObject json = null;
        String uiCallback = null;

        if (state.contains("dataByte")) {
            json = JsonParser
                    .parseString(new String(Base64.getDecoder().decode(state.substring(9, state.length() - 1))))
                    .getAsJsonObject();

            state = json.get(Constants.STATE).getAsString();
            uiCallback = json.get(Connectors.Authorization.UI_CALLBACK).getAsString();
        }
        String error = request.getParameter("error");

        try {
            if (code == null && error.equalsIgnoreCase("access_denied")) {
                return new RedirectView(appendToUrl(uiCallback,
                        ERROR + URLEncoder.encode(new ApiResponse<>(CustomMessages.USER_CANCELED_THE_AUTHORIZATION_PROCESS, error).toString(),
                                StandardCharsets.UTF_8)));
            }

            if (code == null) {
                return new RedirectView(appendToUrl(uiCallback,
                        ERROR + URLEncoder.encode(
                                new ApiResponse<>(error, "Authorization process failed. " + error, error).toString(),
                                StandardCharsets.UTF_8)));
            }

            Optional<ConnectionType> connectionType = connectionTypeService.findById(state);

            if (connectionType.isEmpty()) {
                LOG.debug("connection type not found : {}, using call back : {}", state, uiCallback);
                return new RedirectView(appendToUrl(uiCallback,
                        ERROR + URLEncoder.encode(CustomMessages.CONNECTION_TYPE_NOT_FOUND + " with state: " + state,
                                StandardCharsets.UTF_8)));
            }

            return new RedirectView(appendToUrl(uiCallback, forwardTokenDetails(code, connectionType.get(), json)));

        } catch (Exception e) {
            LOG.error(ExceptionStackTrace.getStackTrace(e));
            return redirectToError(uiCallback, e);
        }
    }

    private RedirectView redirectToError(String uiCallbackUrl, Exception e) {
        return new RedirectView(appendToUrl(uiCallbackUrl, ERROR + new ApiResponse<>("IO-G-AUTH02", e.getMessage())));
    }

    private String appendToUrl(String uiCallbackUrl, String queryParams) {
        String url = null;

        if (uiCallbackUrl.startsWith("http")) {
            url = uiCallbackUrl;
        } else {
            url = configurations.getUiDomain() + uiCallbackUrl;
        }

        char concatWith = '?';
        if (url.contains("?")) {
            concatWith = '&';
        }

        return url + concatWith + queryParams;
    }

    public String forwardTokenDetails(String oauthCode, ConnectionType connectionType, JsonObject stateJson)
            throws IOException, GeneralSecurityException {

        Map<String, Object> connectionProperties = null;
        GoogleTokenResponse tokenResponse = null;
        GoogleIdToken.Payload payload = null;
        /* getting token response using oauthCode */
        LOG.info("REDIRECT URI STRING: {}",configurations.getApiDomain() + configurations.getContextPath() + REDIRECT_URI);
        tokenResponse = new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, GSON_FACTORY, TOKEN_URL, CLIENT_ID,
                CLIENT_SECRET, oauthCode,
                configurations.getApiDomain() + configurations.getContextPath() + REDIRECT_URI).execute();

        tokenResponse.setExpiresInSeconds(null);
        /* verifying the idToken got from response token */
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, GSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID)).build();

        LOG.info("TOKEN RESPOSE: {} ",tokenResponse);

        GoogleIdToken googleIdToken = verifier.verify(tokenResponse.getIdToken());

        /* Getting pay load and users profile details */

        if (googleIdToken != null) {
            payload = googleIdToken.getPayload();

            if (tokenResponse.getRefreshToken() == null) {
                tokenResponse.setRefreshToken(findRefreshToken(connectionType.getId(), payload.getEmail()));
            } else {
                updateAllRefreshToken(tokenResponse.getRefreshToken(), payload.getEmail(), connectionType.getId());
            }

            connectionProperties = new HashMap<>();
            connectionProperties.put(Constants.TOKEN_RESPONSE, tokenResponse);
            connectionProperties.put("payload", payload);

        } else {
            LOG.error("Google Id Token failed : value is {}", googleIdToken);
            throw new IOException("Failed to User Information.");
        }

        /*
         * Creating a connection token for UI. UI will use this connection token to get
         * other details from respective google APIs
         */
        ConnectionToken connectionToken = new ConnectionToken();
        connectionToken.setConnectionTypeId(connectionType.getId());
        connectionToken.setProjectId("PR0090");
        connectionToken.setAccountId("ORG0090");
        connectionToken.setConnectionProperties(connectionProperties);
        connectionToken.setReauthorization(false);

        if (stateJson.has(SETUP_MODE)) {
            connectionToken.addConnectionProperties(SETUP_MODE, stateJson.get(SETUP_MODE).getAsString());
        }

        /*
         * Check here if the connection is being re-authorized, if yes then gather the
         * token details and send it to the UI with re-authorization details.
         */
        if (stateJson.has(Connectors.Authorization.RE_AUTH)
                && stateJson.get(Connectors.Authorization.RE_AUTH).getAsBoolean()) {

            Optional<Connection> connection = connectionRepository.findByIdAndIsDeleted(
                    stateJson.get(Connectors.Authorization.CONNECTION_ID).getAsString(),
                    false);

            if (connection.isEmpty()) {
                throw new IOException("Connection not found for the id : "
                        + stateJson.get(Connectors.Authorization.CONNECTION_ID).getAsString());
            }

            if (!connection.get().isInvalid()) {
                return "error=Connection is already valid." + "&connection-id="
                        + stateJson.get(Connectors.Authorization.CONNECTION_ID).getAsString() + "&re-auth="
                        + stateJson.get(Connectors.Authorization.RE_AUTH).getAsBoolean();
            }

            /*
             * Saving token details in connection token repository and UI can use this
             * connection token to create a connection.
             */

            connectionToken.setConnectionId(connection.get().getId());
            connectionToken.setConnectionName(connection.get().getName());
            connectionToken.setReauthorization(true);
            connectionToken.setConnectionProperties(connectionProperties);
            connectionToken.setConnectionId(stateJson.get(Connectors.Authorization.CONNECTION_ID).getAsString());

            connectionToken = connectionTokenRepository.save(connectionToken);

            return "connection-token=" + URLEncoder.encode(connectionToken.getId(), StandardCharsets.UTF_8)
                    + "&re-auth=" + stateJson.get(Connectors.Authorization.RE_AUTH).getAsBoolean()
                    + "&connection-object-type=" + connectionType.getConnectionTypeDetails().getConnectionObjectType()
                    + "&connection-type-id=" + connectionType.getId() + "&connection-id=" + connection.get().getId()
                    + "&setup-mode=" + stateJson.get(SETUP_MODE);
        }

        connectionToken = connectionTokenRepository.save(connectionToken);

        return "connection-token=" + URLEncoder.encode(connectionToken.getId(), StandardCharsets.UTF_8)
                + "&re-auth=false&connection-object-type=" + connectionType.getConnectionTypeDetails().getConnectionObjectType()
                + "&connection-type-id=" + connectionType.getId() + "&setup-mode=" + stateJson.get(SETUP_MODE);
    }

    public List<Connection> findByConnectionTypeIdAndEmailAndIsDeleted(String connectionTypeId, String email, boolean isDeleted) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Connection> query = cb.createQuery(Connection.class);
        Root<Connection> root = query.from(Connection.class);

        Predicate typePredicate = cb.equal(root.get("connectionTypeId"), connectionTypeId);
        Predicate deletedPredicate = cb.equal(root.get("isDeleted"), isDeleted);

        query.select(root).where(cb.and(typePredicate, deletedPredicate));

        List<Connection> connections = entityManager.createQuery(query).getResultList();

        return connections.stream()
                .filter(c -> c.getProperties() != null &&
                        c.getProperties().containsKey("payload") &&
                        ((Map<String, Object>) c.getProperties().get("payload")).get("email").equals(email))
                .collect(Collectors.toList());
    }


    private void updateAllRefreshToken(String refreshToken, String email, String connectionTypeId) {
        List<Connection> connections = findByConnectionTypeIdAndEmailAndIsDeleted(connectionTypeId,
                connectionTypeId, false);

        ListIterator<Connection> i = connections.listIterator();

        Connection connection = null;

        while (i.hasNext()) {
            connection = i.next();
            GoogleTokenResponse tokenResponse = new ObjectMapper()
                    .convertValue(connection.getProperties().get(Constants.TOKEN_RESPONSE), GoogleTokenResponse.class);
            tokenResponse.setRefreshToken(refreshToken);
            connection.getProperties().put(Constants.TOKEN_RESPONSE, tokenResponse);
            i.set(connection);
        }

        connectionRepository.saveAll(connections);
    }

    private String findRefreshToken(String connectionTypeId, String email) {
        List<Connection> connections = connectionRepository.findByConnectionTypeIdAndEmailAndIsDeleted(connectionTypeId,
                email, false);

        if (!connections.isEmpty()) {

            Map<String, Object> properties = connections.get(0).getProperties();

            GoogleTokenResponse tokenResponse = new ObjectMapper()
                    .convertValue(properties.get(Constants.TOKEN_RESPONSE), GoogleTokenResponse.class);

            return tokenResponse.getRefreshToken();
        }

        return null;
    }
}
