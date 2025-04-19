package com.connection.api.v1.payload.connector.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineSetupPayload {
    @NotNull(message = "name is required.")
    @NotEmpty(message = "name cannot be empty value.")
    private String name;

    @NotNull(message = "sourceConnectionId is required.")
    @NotEmpty(message = "sourceConnectionId cannot be empty value.")
    private String sourceConnectionId;

    @NotNull(message = "targetConnectionId is required.")
    @NotEmpty(message = "targetConnectionId cannot be empty value.")
    private String targetConnectionId;

    @NotNull(message = "projectId is required.")
    @NotEmpty(message = "projectId cannot be empty value.")
    private String projectId;

    @Pattern(regexp = "batch|real time", message = "pipeline type can be either batch or real_time.")
    private String pipelineType;

    private String schemaName;

    private String fileNameExpression;
    private String fileType;

    private List<String> successRecipients = new ArrayList<>();
    private List<String> failureRecipients = new ArrayList<>();
    private Map<String, Object> additionalInfo = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceConnectionId() {
        return sourceConnectionId;
    }

    public void setSourceConnectionId(String sourceConnectionId) {
        this.sourceConnectionId = sourceConnectionId;
    }

    public String getTargetConnectionId() {
        return targetConnectionId;
    }

    public void setTargetConnectionId(String targetConnectionId) {
        this.targetConnectionId = targetConnectionId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPipelineType() {
        return pipelineType;
    }

    public void setPipelineType(String pipelineType) {
        this.pipelineType = pipelineType;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getFileNameExpression() {
        return fileNameExpression;
    }

    public void setFileNameExpression(String fileNameExpression) {
        this.fileNameExpression = fileNameExpression;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<String> getSuccessRecipients() {
        return successRecipients;
    }

    public void setSuccessRecipients(List<String> successRecipients) {
        this.successRecipients = successRecipients;
    }

    public List<String> getFailureRecipients() {
        return failureRecipients;
    }

    public void setFailureRecipients(List<String> failureRecipients) {
        this.failureRecipients = failureRecipients;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
