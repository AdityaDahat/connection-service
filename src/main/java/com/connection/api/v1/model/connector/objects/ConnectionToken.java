package com.connection.api.v1.model.connector.objects;

import com.connection.api.v1.validator.annotation.IDGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "connection_token_tbl")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConnectionToken {
    @Id
    @IDGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @NotNull(message = "connectionTypeId is required")
    @NotEmpty(message = "connectionTypeId is required")
    @Column(name = "connection_type_id")
    private String connectionTypeId;

//    @NotNull(message = "projectId is required")
//    @NotEmpty(message = "projectId is required")
    @Column(name = "project_id")
    private String projectId;

//    @NotNull(message = "accountId is required")
//    @NotEmpty(message = "accountId is required")
    @Column(name = "account_id")
    private String accountId;

//    @NotNull(message = "connectionName is required")
//    @NotEmpty(message = "connectionName is required")
    @Column(name = "connection_name")
    private String connectionName;

    @Column(name = "connection_id")
    private String connectionId;
    @Column(name = "is_reauthorization")
    private boolean isReauthorization;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "connection_properties", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private Map<String, Object> connectionProperties = new HashMap<>();

    public ConnectionToken() {
    }

    public ConnectionToken(String id, String connectionTypeId, String projectId, String accountId, String connectionName, String connectionId, boolean isReauthorization, String userId, String userName, Instant createdAt, Map<String, Object> connectionProperties) {
        this.id = id;
        this.connectionTypeId = connectionTypeId;
        this.projectId = projectId;
        this.accountId = accountId;
        this.connectionName = connectionName;
        this.connectionId = connectionId;
        this.isReauthorization = isReauthorization;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.connectionProperties = connectionProperties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public boolean isReauthorization() {
        return isReauthorization;
    }

    public void setReauthorization(boolean reauthorization) {
        isReauthorization = reauthorization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Object> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Map<String, Object> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public void addConnectionProperties(String key, Object value) {
        connectionProperties.put(key, value);
    }
}
