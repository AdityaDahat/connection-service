package com.connection.api.v1.model.connector.management.dto;
import com.connection.api.v1.model.connector.management.ConnectionTypeDetails;
import java.util.Map;

public interface ConnectionTypeProjection {
    String getName();
    String getShortName();
    Boolean getIsPrivate();
    String getStatus();
    Boolean getIsGlobalConnectionType();
    Boolean getIsDeleted();
    ConnectionTypeDetails getConnectionTypeDetails();

    default String getProvider() {
        return getConnectionTypeDetails() != null ? getConnectionTypeDetails().getProvider() : "";
    }

    String getType();

    default String getAuthenticationMode() {
        return getConnectionTypeDetails() != null ? getConnectionTypeDetails().getProvider() : "";
    }

    default String getConnectionObjectType() {
        return getConnectionTypeDetails() != null ? getConnectionTypeDetails().getConnectionObjectType() : "";
    }

    default Map<String, Object> getAdditionalInfo() {
        return getConnectionTypeDetails() != null ? getConnectionTypeDetails().getAdditionalInfo() : null;
    }
}
