package com.connection.api.v1.model.connector.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class IntermediateTemplate {
    @NotNull(message = "name should not be null/empty")
    @NotEmpty(message = "name should not be null/empty")
    private String name;
    @NotNull(message = "templateType should not be null/empty")
    @NotEmpty(message = "templateType should not be null/empty")
    private String templateType;

    @NotNull(message = "connectionTypeId should not be null/empty")
    @NotEmpty(message = "connectionTypeId should not be null/empty")
    private String connectionTypeId;

    @NotNull(message = "connectionTypeName should not be null/empty")
    @NotEmpty(message = "connectionTypeName should not be null/empty")
    private String connectionTypeName;

    @JsonIgnore
    private boolean isDefinitionsRequired;

    private Map<String, Object> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
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

    public boolean isDefinitionsRequired() {
        return isDefinitionsRequired;
    }

    public void setDefinitionsRequired(boolean definitionsRequired) {
        isDefinitionsRequired = definitionsRequired;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
