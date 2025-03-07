package com.connection.api.v1.model.connector.management;

import com.connection.api.v1.model.common.Metadata;
import com.connection.api.v1.validator.annotation.IDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;
@Entity
@Table(name = "connection_type_categories_tbl")
public class
ConnectionTypeCategory {

    public static final String DEFAULT = "connectionTypeCategory";
    public static final String SOURCE_CATEGORY = "sourceCategory";
    public static final String TARGET_CATEGORY = "targetCategory";
    @Id
    @IDGenerator
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_info", columnDefinition = "JSONB DEFAULT '{}'::JSONB")
    private Map<String, Object> additionalInfo = new HashMap<>();
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "JSONB DEFAULT '{ }'::JSONB")
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
