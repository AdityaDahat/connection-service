package com.connection.api.v1.model.connector.management;

import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.validator.annotation.IDGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pipeline_template_tbl")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PipelineTemplate {
    @Id
    @IDGenerator
    private String id;
    @Column(name = "pipeline_type")
    private String pipelineType;

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotNull(message = "shortName cannot be null")
    @NotEmpty(message = "shortName cannot be empty")
    @Column(name = "short_name")
    private String shortName;

    @NotNull(message = "description cannot be null")
    @NotEmpty(message = "description cannot be empty")
    @Column(name = "description")
    private String description;

    @Valid
    @NotNull(message = "intermediateTemplates cannot be null")
    @NotEmpty(message = "intermediateTemplates cannot be empty")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "intermediate_templates")
    private List<IntermediateTemplate> intermediateTemplates = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_info")
    private Map<String, Object> additionalInfo = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    private List<DisplayProperty> pipelineHistoryDisplayProperties = new ArrayList<>();

    /**
     * Add fields to this array that you want to include in the flow details
     * response if those fields are not coming with the response.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> includePipelineFields = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> excludePipelineFields = new ArrayList<>();

    private Metadata metadata;
    private String status;
    @NotNull(message = "mode cannot be null")
    @NotEmpty(message = "mode cannot be empty")
    private String mode;
    private boolean isDeleted;

    public PipelineTemplate() {
    }

    public PipelineTemplate(String id, String pipelineType, String name, String shortName, String description, List<IntermediateTemplate> intermediateTemplates, Map<String, Object> additionalInfo, List<DisplayProperty> pipelineHistoryDisplayProperties, List<String> includePipelineFields, List<String> excludePipelineFields, Metadata metadata, String status, String mode, boolean isDeleted) {
        this.id = id;
        this.pipelineType = pipelineType;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.intermediateTemplates = intermediateTemplates;
        this.additionalInfo = additionalInfo;
        this.pipelineHistoryDisplayProperties = pipelineHistoryDisplayProperties;
        this.includePipelineFields = includePipelineFields;
        this.excludePipelineFields = excludePipelineFields;
        this.metadata = metadata;
        this.status = status;
        this.mode = mode;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPipelineType() {
        return pipelineType;
    }

    public void setPipelineType(String pipelineType) {
        this.pipelineType = pipelineType;
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

    public List<IntermediateTemplate> getIntermediateTemplates() {
        return intermediateTemplates;
    }

    public void setIntermediateTemplates(List<IntermediateTemplate> intermediateTemplates) {
        this.intermediateTemplates = intermediateTemplates;
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
