package com.connection.api.v1.model.connector.management;

import com.connection.api.v1.model.common.Metadata;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.connection.api.v1.validator.annotation.IDGenerator;
import com.connection.api.v1.validator.annotation.ValidateOperationMode;

@Entity
@Table(name = "connection_type_tbl")
public class ConnectionType {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @IDGenerator
    private String id;
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    @Column(name = "name")
    private String name;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "short_code")
    private String shortCode;
    @Column(name = "type")
    private String type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "connection_type_details", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private ConnectionTypeDetails connectionTypeDetails;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private Metadata metadata;
    @Column(name = "skip_flow_scripts")
    private boolean skipFlowScripts;
    @Column(name = "is_global_connection_type")
    private boolean isGlobalConnectionType;
    @Column(name = "is_private")
    private boolean isPrivate;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "version")
    private Float version = Float.valueOf(0);
    @ValidateOperationMode
    @Column(name = "status")
    private String status = OperationModes.COMING_SOON.toString();

    public ConnectionType() {
    }

    public ConnectionType(String id, String name, String shortName, String shortCode, String type, ConnectionTypeDetails connectionTypeDetails, Metadata metadata, boolean skipFlowScripts, boolean isGlobalConnectionType, boolean isPrivate, boolean isDeleted, String status) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.shortCode = shortCode;
        this.type = type;
        this.connectionTypeDetails = connectionTypeDetails;
        this.metadata = metadata;
        this.skipFlowScripts = skipFlowScripts;
        this.isGlobalConnectionType = isGlobalConnectionType;
        this.isPrivate = isPrivate;
        this.isDeleted = isDeleted;
        this.status = status;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ConnectionTypeDetails getConnectionTypeDetails() {
        return connectionTypeDetails;
    }

    public void setConnectionTypeDetails(ConnectionTypeDetails connectionTypeDetails) {
        this.connectionTypeDetails = connectionTypeDetails;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public boolean isSkipFlowScripts() {
        return skipFlowScripts;
    }

    public void setSkipFlowScripts(boolean skipFlowScripts) {
        this.skipFlowScripts = skipFlowScripts;
    }

    public boolean isGlobalConnectionType() {
        return isGlobalConnectionType;
    }

    public void setGlobalConnectionType(boolean globalConnectionType) {
        isGlobalConnectionType = globalConnectionType;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Float getVersion() {
        return version;
    }

    public void setVersion(Float version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
