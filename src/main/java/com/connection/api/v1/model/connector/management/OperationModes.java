package com.connection.api.v1.model.connector.management;

public enum OperationModes {
    PRODUCTION_READY("production_ready"), BETA_VERSION("beta_version"), COMING_SOON("coming_soon"),
    PREVIEW("preview"),
    DEPRECATED("deprecated");

    OperationModes(String status) {
        this.status = status;
    }

    private String status;

    @Override
    public String toString() {
        return this.status;
    }
}
