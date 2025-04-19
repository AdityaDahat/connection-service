package com.connection.api.v1.model.connector.objects;

import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.model.connector.management.Attribute;
import com.connection.api.v1.validator.annotation.IDGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "connection_tbl")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Connection {
    @Id
    @IDGenerator
    private String id;
    @NotNull(message = "name is required")
    @NotEmpty(message = "name is required")
    private String name;
    private String normalizedName;
    private String shortCode;
    @NotNull(message = "typeId cannot be null")
    private String connectionTypeId;
    private String connectionTypeName;
    private String connectionObjectType;
    private String processType;
    private String category;
    private String accountId;
    private String accountCode;
    private String accountName;
    @NotEmpty(message = ("projectId cannot be empty"))
    @NotNull(message = ("projectId cannot be null"))
    private String projectId;
    private String projectCode;
    private String projectName;
    private Map<String, Object> properties = new LinkedHashMap<>();
    private List<Attribute> customAttributes = new ArrayList<>();
    private Metadata metadata;
    private boolean isGlobalConnection;
    private boolean isEditable;
    private boolean isDeleted;
    private boolean isInvalid;
    private String invalidationReason;
    private boolean isUpgradeAvailable;
    private Float version = Float.valueOf(0);
    private boolean isPrivate;

    public Connection() {
    }

    public Connection(String id, String name, String normalizedName, String shortCode, String connectionTypeId, String connectionTypeName, String connectionObjectType, String processType, String category, String accountId, String accountCode, String accountName, String projectId, String projectCode, String projectName, Map<String, Object> properties, List<Attribute> customAttributes, Metadata metadata, boolean isGlobalConnection, boolean isEditable, boolean isDeleted, boolean isInvalid, String invalidationReason, boolean isUpgradeAvailable, Float version, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.normalizedName = normalizedName;
        this.shortCode = shortCode;
        this.connectionTypeId = connectionTypeId;
        this.connectionTypeName = connectionTypeName;
        this.connectionObjectType = connectionObjectType;
        this.processType = processType;
        this.category = category;
        this.accountId = accountId;
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.properties = properties;
        this.customAttributes = customAttributes;
        this.metadata = metadata;
        this.isGlobalConnection = isGlobalConnection;
        this.isEditable = isEditable;
        this.isDeleted = isDeleted;
        this.isInvalid = isInvalid;
        this.invalidationReason = invalidationReason;
        this.isUpgradeAvailable = isUpgradeAvailable;
        this.version = version;
        this.isPrivate = isPrivate;
    }


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

    public String getConnectionObjectType() {
        return connectionObjectType;
    }

    public void setConnectionObjectType(String connectionObjectType) {
        this.connectionObjectType = connectionObjectType;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public boolean isGlobalConnection() {
        return isGlobalConnection;
    }

    public void setGlobalConnection(boolean globalConnection) {
        isGlobalConnection = globalConnection;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
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

    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public String getInvalidationReason() {
        return invalidationReason;
    }

    public void setInvalidationReason(String invalidationReason) {
        this.invalidationReason = invalidationReason;
    }

    public boolean isUpgradeAvailable() {
        return isUpgradeAvailable;
    }

    public void setUpgradeAvailable(boolean upgradeAvailable) {
        isUpgradeAvailable = upgradeAvailable;
    }

    public Float getVersion() {
        return version;
    }

    public void setVersion(Float version) {
        this.version = version;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
