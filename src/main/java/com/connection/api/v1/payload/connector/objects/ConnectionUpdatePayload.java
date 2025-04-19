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

public class ConnectionUpdatePayload {

    @NotNull(message = "Connection id is required.")
    @NotEmpty(message = "Connection id is required.")
    private String id;

    private String connectionToken;

    private Boolean isReAuth;

    @NotNull(message = "name is required")
    @NotEmpty
    @Pattern(regexp = "^[\\p{Alnum} .'-_]+$", message = "name must a alphanumeric value")
    @Size(min = 3, max = 50)
    private String name;

    private Map<String, Object> properties = new LinkedHashMap<>();

    private List<Attribute> customAttributes = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionToken() {
        return connectionToken;
    }

    public void setConnectionToken(String connectionToken) {
        this.connectionToken = connectionToken;
    }

    public Boolean getReAuth() {
        return isReAuth;
    }

    public void setReAuth(Boolean reAuth) {
        isReAuth = reAuth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
