package com.connection.api.v1.payload.connector.management;

import com.connection.api.v1.model.connector.management.Attribute;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionDefinitionCreationPayload {

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;
    private String displayName;
    @NotNull(message = "objectType cannot be null")
    @NotEmpty(message = "objectType cannot be empty")
    private String objectType;
    @NotNull(message = "connectionTypeId cannot be null")
    @NotEmpty(message = "connectionTypeId cannot be empty")
    private String connectionTypeId;
    @NotNull(message = "connectionTypeName cannot be null")
    @NotEmpty(message = "connectionTypeName cannot be empty")
    private String connectionTypeName;
    @NotNull(message = "version cannot be null")
    @NotEmpty(message = "version cannot be empty")
    private String version;
    private List<Attribute> attributes = new ArrayList<>();
    private Map<String, Object> other = new HashMap<>();
    private String mode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(String connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public String getConnectionTypeName() {
        return connectionTypeName;
    }

    public void setConnectionTypeName(String connectionTypeName) {
        this.connectionTypeName = connectionTypeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
