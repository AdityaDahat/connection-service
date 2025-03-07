package com.connection.api.v1.model.connector.management;

import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.validator.annotation.IDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "connection_definition_tbl")
public class ConnectionDefinition {

    public static final String SOURCE_DEFINITION = "sourceDefinition";
    public static final String TARGET_DEFINITION = "targetDefinition";
    @Id
    @IDGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    @Column(name = "name")
    private String name;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "object_type")
    private String objectType;
    @NotNull(message = "connectionTypeId cannot be null")
    @NotEmpty(message = "connectionTypeId cannot be empty")
    @Column(name = "connection_type_id")
    private String connectionTypeId;
    @NotNull(message = "connectionTypeName cannot be null")
    @NotEmpty(message = "connectionTypeName cannot be empty")
    @Column(name = "connection_type_name")
    private String connectionTypeName;
    @NotNull(message = "version cannot be null")
    @NotEmpty(message = "version cannot be empty")
    @Column(name = "version")
    private String version;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "JSONB DEFAULT '[]'::JSONB")
    private List<Attribute> attributes = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "other", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private Map<String, Object> other = new HashMap<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private Metadata metadata;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "mode")
    private String mode;

    public ConnectionDefinition() {
    }

    public ConnectionDefinition(String id, String name, String displayName, String objectType, String connectionTypeId, String connectionTypeName, String version, List<Attribute> attributes, Map<String, Object> other, Metadata metadata, boolean isDeleted, String mode) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.objectType = objectType;
        this.connectionTypeId = connectionTypeId;
        this.connectionTypeName = connectionTypeName;
        this.version = version;
        this.attributes = attributes;
        this.other = other;
        this.metadata = metadata;
        this.isDeleted = isDeleted;
        this.mode = mode;
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
