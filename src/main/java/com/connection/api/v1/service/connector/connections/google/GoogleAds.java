package com.connection.api.v1.service.connector.connections.google;

import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.objects.Connection;
import com.connection.api.v1.model.connector.objects.ConnectionToken;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.model.response.ErrorMessage;
import com.connection.api.v1.repository.connector.management.ConnectionTypeRepository;
import com.connection.api.v1.repository.connector.objects.ConnectionRepository;
import com.connection.api.v1.service.connector.objects.authorization.GoogleOAuth2Service;
import com.connection.constants.Constants;
import com.connection.exception.ApiException;
import com.connection.exception.ExceptionStackTrace;
import com.connection.exception.RestClientExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GoogleAds {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleAds.class);
    private static final String CONNECTION_TYPE_ID = "xyz";
    private static final String CUSTOMER_ID = "customerId";
    private static final String DESCRIPTIVE_NAME = "descriptiveName";
    private static final String DATE_TIMEZONE = "dateTimeZone";
    private final GoogleOAuth2Service googleOAuth2Service;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionRepository connectionRepository;
    private final RestTemplate restTemplate;

    public GoogleAds(GoogleOAuth2Service googleOAuth2Service,
                     ConnectionTypeRepository connectionTypeRepository,
                     ConnectionRepository connectionRepository,
                     RestTemplate restTemplate) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionRepository = connectionRepository;
        this.restTemplate = restTemplate;
    }

    public List<Object> getAccountIds(String connectionTokenId) {
        Optional<ConnectionToken> connectionToken = googleOAuth2Service.getConnectionToken(connectionTokenId,
                CONNECTION_TYPE_ID);

        if (connectionToken.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.CONNECTION_TOKEN_NOT_FOUND));

        return getAccountIds(connectionToken.get());
    }


    private List<Object> getAccountIds(ConnectionToken connectionToken) {
        try {
            Optional<ConnectionType> connectionType = connectionTypeRepository.findByIdAndIsDeleted(CONNECTION_TYPE_ID,
                    false);
            if (connectionType.isEmpty())
                throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                        ErrorMessage.INVALID_CONNECTION_TYPE));

            TokenResponse tokenResponse = new ObjectMapper().convertValue(
                    connectionToken.getConnectionProperties().get(Constants.TOKEN_RESPONSE), TokenResponse.class);

            List<String> customerResourceNames = getCustomerResourceName(tokenResponse, connectionType);

            if (customerResourceNames.isEmpty()) {
                LOG.info("No accessible customers found.");
                throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                        "Failed to get the customer ids."));
            }

            LOG.debug("You are logged in as a user with access to the following customers:");
            List<Object> validCustomers = new ArrayList<>();
            for (String customerResourceName : customerResourceNames) {
                String customerId = customerResourceName.substring(customerResourceName.lastIndexOf("/") + 1);
                Map<String, Object> customerMap = fetchCustomerDetails(customerId, connectionType.get(), tokenResponse);
                if (customerMap != null && !customerMap.isEmpty())
                    validCustomers.add(customerMap);
            }
            if (validCustomers.isEmpty())
                throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                        "You are trying to add a manager account. Reports can only be obtained for advertiser (non-manager) accounts. To get data, please reconnect Google Ads using advertiser (non-manager) account."));
            return validCustomers;
        } catch (Exception e) {
            LOG.error(ExceptionStackTrace.getStackTrace(e));
        }

        throw new ApiException(HttpStatus.BAD_REQUEST.value(),
                "Failed to get the customer ids.");
    }

    private List<String> getCustomerResourceName(TokenResponse tokenResponse, Optional<ConnectionType> connectionType) {
        restTemplate.setErrorHandler(new RestClientExceptionHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenResponse.getAccessToken());
        headers.set("developer-token", GoogleOAuth2Service.DEVELOPER_TOKEN);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(new HashMap<>(), headers);
        ConnectionType connectionType1 = connectionType.get();
        String url = ((String) connectionType1.getConnectionTypeDetails().getAdditionalInfo().get("customerListUrl"))
                .replace(Constants.VERSION_EXPRESSION, connectionType1.getConnectionTypeDetails().getVersion());

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        if (response.getStatusCode().isError()) {
            throw new ApiException(new ApiResponse<>(response.getBody(), HttpStatus.BAD_REQUEST.value()));
        }
        Map<String, Object> responseBody = response.getBody();
        return (List<String>) responseBody.get("resourceNames");
    }

    private Map<String, Object> fetchCustomerDetails(String customerId, ConnectionType connectionType, TokenResponse tokenResponse) {
        try {
            ResponseEntity<Map> responseEntity = getCustomerDetails(customerId, connectionType, tokenResponse);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = responseEntity.getBody();
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");

                if (results != null && !results.isEmpty()) {
                    Map<String, Object> result = results.get(0);
                    Map<String, Object> customer = (Map<String, Object>) result.get("customer");

                    if (!(boolean) customer.get("manager")) {
                        Map<String, Object> mapCustomer = new HashMap<>();
                        mapCustomer.put(CUSTOMER_ID, Long.parseLong(customer.get("id").toString()));
                        mapCustomer.put("currencyCode", customer.get("currencyCode"));
                        mapCustomer.put(DESCRIPTIVE_NAME, customer.get("descriptiveName"));
                        mapCustomer.put("canManageClients", customer.get("manager"));
                        mapCustomer.put("testAccount", customer.get("testAccount"));
                        mapCustomer.put(DATE_TIMEZONE, customer.get("timeZone"));
                        return mapCustomer;
                    }
                }
            }
        } catch (RestClientException e) {
            LOG.error("Error communicating with the Google Ads API: {}", e.getMessage());
        }
        return Map.of();
    }

    private ResponseEntity<Map> getCustomerDetails(String customerId, ConnectionType connectionType, TokenResponse tokenResponse) {
        String apiUrl = ((String) connectionType.getConnectionTypeDetails().getAdditionalInfo().get("customerDetailsUrl"))
                .replace(Constants.VERSION_EXPRESSION, connectionType.getConnectionTypeDetails().getVersion()).replace("${customerId}", customerId);

        String query = "SELECT customer.id, " +
                "customer.descriptive_name, " +
                "customer.currency_code, " +
                "customer.time_zone, " +
                "customer.test_account, " +
                "customer.tracking_url_template, " +
                "customer.auto_tagging_enabled, " +
                "customer.manager " +
                "FROM customer " +
                "LIMIT 1";

        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenResponse.getAccessToken());
        headers.set("developer-token", GoogleOAuth2Service.DEVELOPER_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );
    }

    public void isCustomerIdInUse(long customerId, String projectId) {

        Optional<Connection> connection = connectionRepository
                .findByProjectIdAndIsDeletedAndCustomerIdAndConnectionTypeId(projectId,
                        false, customerId, CONNECTION_TYPE_ID);

        if (connection.isPresent())
            throw new ApiException(new ApiResponse<>(HttpStatus.NOT_ACCEPTABLE.value(),
                    "Account Id " + customerId + " is already used in connection " + connection.get().getName()));
    }
}

