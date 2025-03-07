package com.connection.api.v1.payload.connector.management;

import com.connection.api.v1.model.connector.management.ConnectionTypeDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ConnectionTypeCreationPayload {
    @NotEmpty(message = "The connection type name is required.")
    private String name;

    @NotEmpty(message = "The shortName is required.")
    private String shortName;

    @Valid
    @NotNull
    private ConnectionTypeDetails connectionTypeDetails;

    private Integer sequenceIndex;
    private Float version = Float.valueOf(0);
    private boolean skipFlowScripts;
    private boolean isGlobalConnectionType;
    private boolean isPrivate;

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

    public ConnectionTypeDetails getConnectionTypeDetails() {
        return connectionTypeDetails;
    }

    public void setConnectionTypeDetails(ConnectionTypeDetails connectionTypeDetails) {
        this.connectionTypeDetails = connectionTypeDetails;
    }

    public Integer getSequenceIndex() {
        return sequenceIndex;
    }

    public void setSequenceIndex(Integer sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    public Float getVersion() {
        return version;
    }

    public void setVersion(Float version) {
        this.version = version;
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
}
