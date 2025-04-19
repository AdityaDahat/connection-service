package com.connection.api.v1.model.connector.objects;

import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.validator.annotation.IDGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pipeline_tbl")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pipeline {
    @Id
    @IDGenerator
    private String id;
    private String name;
    private String normalizedName;
    private String shortCode;
    private String destination;
    @NotNull(message = "pipeline type cannot be null")
    @NotEmpty(message = "pipeline type cannot be empty")
    private String pipelineType;
    @NotNull(message = "organizationId cannot be null")
    @NotEmpty(message = "organizationId cannot be empty")
    private String accountId;
    @NotNull(message = "organizationName cannot be null")
    @NotEmpty(message = "organizationName cannot be empty")
    private String accountName;
    private String entityType;
    @NotNull(message = "projectId cannot be null")
    @NotEmpty(message = "projectId cannot be empty")
    private String projectId;
    @NotNull(message = "projectName cannot be null")
    @NotEmpty(message = "projectName cannot be empty")
    private String projectName;
    private String pipelineId;
    private String pipelineName;
    private String pipelineShortName;
    private String pipelineDescription;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Process> processes = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private Schedule schedule;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> properties = new LinkedHashMap<>();
    private boolean isScriptExecuted;
    private boolean isAirflowDagReady;
    private String status;
    private String statusDescription;
    private Integer statusTransactionCount = 0;
    private boolean isDeleted;
    private boolean isInvalid;
    private String invalidationReason;
    private boolean isEnabled;
    private String pausedBy;
    private Metadata metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPipelineType() {
        return pipelineType;
    }

    public void setPipelineType(String pipelineType) {
        this.pipelineType = pipelineType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getPipelineShortName() {
        return pipelineShortName;
    }

    public void setPipelineShortName(String pipelineShortName) {
        this.pipelineShortName = pipelineShortName;
    }

    public String getPipelineDescription() {
        return pipelineDescription;
    }

    public void setPipelineDescription(String pipelineDescription) {
        this.pipelineDescription = pipelineDescription;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public boolean isScriptExecuted() {
        return isScriptExecuted;
    }

    public void setScriptExecuted(boolean scriptExecuted) {
        isScriptExecuted = scriptExecuted;
    }

    public boolean isAirflowDagReady() {
        return isAirflowDagReady;
    }

    public void setAirflowDagReady(boolean airflowDagReady) {
        isAirflowDagReady = airflowDagReady;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Integer getStatusTransactionCount() {
        return statusTransactionCount;
    }

    public void setStatusTransactionCount(Integer statusTransactionCount) {
        this.statusTransactionCount = statusTransactionCount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public String getInvalidationReason() {
        return invalidationReason;
    }

    public void setInvalidationReason(String invalidationReason) {
        this.invalidationReason = invalidationReason;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getPausedBy() {
        return pausedBy;
    }

    public void setPausedBy(String pausedBy) {
        this.pausedBy = pausedBy;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
