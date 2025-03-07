package com.connection.api.v1.model.connector.management;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.connection.api.v1.validator.annotation.ValidateConnectorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConnectionTypeDetails {
    private String authenticationMode;
    @NotNull(message = "provider cannot be null")
    private String provider;
    private String version;
    @ValidateConnectorType
    private String type;
    private String processType;
    private String category;
    @NotNull(message = "Category id cannot be null.")
    @NotEmpty(message = "Category id cannot be empty.")
    private String categoryId;
    private String prefix;
    private String implementingClass;
    private String description;
    private List<String> scopes = new ArrayList<>();
    private String authorizationURL;
    private String connectionObjectType;
    private String scheduleType;
    private String dateRangeType = DateRangeType.DATE_RANGE.getType();
    private List<ClientCredentials> clientCredentials = new ArrayList<>();
    private Map<String,Object> additionalInfo;
    private List<DisplayProperty> displayProperties = new ArrayList<>();
    private Map<String, Object> icon = new HashMap<>();
    private boolean isConnectionTokenRequired;


    public ConnectionTypeDetails() {
    }

    public ConnectionTypeDetails(String authenticationMode, String provider, String version, String type, String processType, String category, String categoryId, String prefix, String implementingClass, String description, List<String> scopes, String authorizationURL, String connectionObjectType, String scheduleType, String dateRangeType, List<ClientCredentials> clientCredentials, Map<String, Object> additionalInfo, List<DisplayProperty> displayProperties, Map<String, Object> icon, boolean isConnectionTokenRequired) {
        this.authenticationMode = authenticationMode;
        this.provider = provider;
        this.version = version;
        this.type = type;
        this.processType = processType;
        this.category = category;
        this.categoryId = categoryId;
        this.prefix = prefix;
        this.implementingClass = implementingClass;
        this.description = description;
        this.scopes = scopes;
        this.authorizationURL = authorizationURL;
        this.connectionObjectType = connectionObjectType;
        this.scheduleType = scheduleType;
        this.dateRangeType = dateRangeType;
        this.clientCredentials = clientCredentials;
        this.additionalInfo = additionalInfo;
        this.displayProperties = displayProperties;
        this.icon = icon;
        this.isConnectionTokenRequired = isConnectionTokenRequired;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getImplementingClass() {
        return implementingClass;
    }

    public void setImplementingClass(String implementingClass) {
        this.implementingClass = implementingClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getAuthorizationURL() {
        return authorizationURL;
    }

    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
    }

    public String getConnectionObjectType() {
        return connectionObjectType;
    }

    public void setConnectionObjectType(String connectionObjectType) {
        this.connectionObjectType = connectionObjectType;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getDateRangeType() {
        return dateRangeType;
    }

    public void setDateRangeType(String dateRangeType) {
        this.dateRangeType = dateRangeType;
    }

    public List<ClientCredentials> getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(List<ClientCredentials> clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<DisplayProperty> getDisplayProperties() {
        return displayProperties;
    }

    public void setDisplayProperties(List<DisplayProperty> displayProperties) {
        this.displayProperties = displayProperties;
    }

    public Map<String, Object> getIcon() {
        return icon;
    }

    public void setIcon(Map<String, Object> icon) {
        this.icon = icon;
    }

    public boolean isConnectionTokenRequired() {
        return isConnectionTokenRequired;
    }

    public void setConnectionTokenRequired(boolean connectionTokenRequired) {
        isConnectionTokenRequired = connectionTokenRequired;
    }
}
