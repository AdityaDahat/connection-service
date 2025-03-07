package com.connection.api.v1.payload.connector.objects;

import com.connection.api.v1.model.connector.management.Attribute;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionSetupPayload {

    private String connectionToken;

    private Boolean isReAuth;

    @NotNull(message = "name is required")
    @NotEmpty
    @Pattern(regexp = "^[\\p{Alnum} .'-_]+$", message = "name must a alphanumeric value")
    @Size(min = 3, max = 50)
    private String name;

    @NotEmpty(message = "Connection Type Id is required")
    private String connectionTypeId;

    @NotEmpty(message = ("projectId cannot be empty"))
    @NotNull(message = ("projectId cannot be null"))
    private String projectId;

    private Map<String, Object> properties = new LinkedHashMap<>();

    private List<Attribute> customAttributes = new ArrayList<>();

    public String getConnectionToken() {
        return connectionToken;
    }

    public void setConnectionToken(String connectionToken) {
        this.connectionToken = connectionToken;
    }

    public Boolean getIsReAuth() {
        return isReAuth;
    }

    public void setIsReAuth(Boolean isReAuth) {
        this.isReAuth = isReAuth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(String connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<Attribute> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<Attribute> customAttributes) {
        this.customAttributes = customAttributes;
    }

}
