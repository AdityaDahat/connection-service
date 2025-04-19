package com.connection.api.v1.model.connector.objects;

import com.connection.api.v1.model.common.Metadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Process {

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotNull(message = "objectType cannot be null")
    @NotEmpty(message = "objectType cannot be empty")
    private String objectType;

    private String PipelineCode;
    private String accountCode;
    private String projectCode;

    @NotNull(message = "sourceConnection cannot be null")
    private Connection sourceConnection;
    private Map<String, Object> sourceDefinition = new HashMap<>();
    private Map<String, Object> sourceTransform = new HashMap<>();
    private Map<String, Object> sourceProperties = new HashMap<>();
    @NotNull(message = "targetConnection cannot be null")
    private Connection targetConnection;
    private Map<String, Object> targetDefinition = new HashMap<>();
    private Map<String, Object> targetTransform = new HashMap<>();
    private Map<String, Object> targetProperties = new HashMap<>();
    private List<Object> keyAttributes = new ArrayList<>();
    private List<Object> expression = new ArrayList<>();
    private Map<String, Object> other = new HashMap<>();
    private Metadata metadata;

    public Process addSourceDefinition(String key, Object value) {
        this.sourceDefinition.put(key, value);
        return this;
    }

    public Process addSourceTransform(String key, Object value) {
        this.sourceTransform.put(key, value);
        return this;
    }

    public Process addSourceProperty(String key, Object value) {
        this.sourceProperties.put(key, value);
        return this;
    }

    public Process addTargetDefinition(String key, Object value) {
        this.targetDefinition.put(key, value);
        return this;
    }

    public Process addTargetTransform(String key, Object value) {
        this.targetTransform.put(key, value);
        return this;
    }

    public Process addTargetProperty(String key, Object value) {
        this.targetProperties.put(key, value);
        return this;
    }

    public Process addKeyAttribute(Object value) {
        this.keyAttributes.add(value);
        return this;
    }

    public Process addExpression(Object value) {
        this.expression.add(value);
        return this;
    }

    public Process addOther(String key, Object value) {
        this.other.put(key, value);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getPipelineCode() {
        return PipelineCode;
    }

    public void setPipelineCode(String pipelineCode) {
        PipelineCode = pipelineCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Connection getSourceConnection() {
        return sourceConnection;
    }

    public void setSourceConnection(Connection sourceConnection) {
        this.sourceConnection = sourceConnection;
    }

    public Map<String, Object> getSourceDefinition() {
        return sourceDefinition;
    }

    public void setSourceDefinition(Map<String, Object> sourceDefinition) {
        this.sourceDefinition = sourceDefinition;
    }

    public Map<String, Object> getSourceTransform() {
        return sourceTransform;
    }

    public void setSourceTransform(Map<String, Object> sourceTransform) {
        this.sourceTransform = sourceTransform;
    }

    public Map<String, Object> getSourceProperties() {
        return sourceProperties;
    }

    public void setSourceProperties(Map<String, Object> sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    public Connection getTargetConnection() {
        return targetConnection;
    }

    public void setTargetConnection(Connection targetConnection) {
        this.targetConnection = targetConnection;
    }

    public Map<String, Object> getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(Map<String, Object> targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    public Map<String, Object> getTargetTransform() {
        return targetTransform;
    }

    public void setTargetTransform(Map<String, Object> targetTransform) {
        this.targetTransform = targetTransform;
    }

    public Map<String, Object> getTargetProperties() {
        return targetProperties;
    }

    public void setTargetProperties(Map<String, Object> targetProperties) {
        this.targetProperties = targetProperties;
    }

    public List<Object> getKeyAttributes() {
        return keyAttributes;
    }

    public void setKeyAttributes(List<Object> keyAttributes) {
        this.keyAttributes = keyAttributes;
    }

    public List<Object> getExpression() {
        return expression;
    }

    public void setExpression(List<Object> expression) {
        this.expression = expression;
    }

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
