package com.connection.api.v1.payload.connector.management;

import com.connection.api.v1.model.connector.management.DisplayProperty;
import com.connection.api.v1.model.connector.management.IntermediateTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineTemplateCreationPayload {

    @NotNull(message = "PipelineType cannot be null")
    @NotEmpty(message = "PipelineType cannot be empty")
    private String PipelineType;

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotNull(message = "shortName cannot be null")
    @NotEmpty(message = "shortName cannot be empty")
    private String shortName;

    @NotNull(message = "description cannot be null")
    @NotEmpty(message = "description cannot be empty")
    private String description;

    @Valid
    @NotNull(message = "intermediateTemplate cannot be null")
    @NotEmpty(message = "intermediateTemplate cannot be empty")
    private List<IntermediateTemplate> intermediateTemplate = new ArrayList<>();

    private List<String> includePipelineFields = new ArrayList<>();
    private List<String> excludePipelineFields = new ArrayList<>();
    private Map<String, Object> additionalInfo = new HashMap<>();
    private List<DisplayProperty> pipelineHistoryDisplayProperties = new ArrayList<>();
    private String mode;
    private String status;

    public String getPipelineType() {
        return PipelineType;
    }

    public void setPipelineType(String pipelineType) {
        PipelineType = pipelineType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IntermediateTemplate> getIntermediateTemplate() {
        return intermediateTemplate;
    }

    public void setIntermediateTemplate(List<IntermediateTemplate> intermediateTemplate) {
        this.intermediateTemplate = intermediateTemplate;
    }

    public List<String> getIncludePipelineFields() {
        return includePipelineFields;
    }

    public void setIncludePipelineFields(List<String> includePipelineFields) {
        this.includePipelineFields = includePipelineFields;
    }

    public List<String> getExcludePipelineFields() {
        return excludePipelineFields;
    }

    public void setExcludePipelineFields(List<String> excludePipelineFields) {
        this.excludePipelineFields = excludePipelineFields;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<DisplayProperty> getPipelineHistoryDisplayProperties() {
        return pipelineHistoryDisplayProperties;
    }

    public void setPipelineHistoryDisplayProperties(List<DisplayProperty> pipelineHistoryDisplayProperties) {
        this.pipelineHistoryDisplayProperties = pipelineHistoryDisplayProperties;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
