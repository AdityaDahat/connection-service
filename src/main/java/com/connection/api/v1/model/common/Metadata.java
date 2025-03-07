package com.connection.api.v1.model.common;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;

public class Metadata implements Serializable {
    private static final long serialVersionUID = -2098135135943933369L;

    @DateTimeFormat(iso = DateTimeFormat.ISO.NONE)
    private Instant createdOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.NONE)
    private Instant modifiedOn;

    @NotNull
    private String createdBy;

    private String creatorName;

    @NotNull
    private String modifiedBy;
    private String modifierName;

    private String timeZone;

    public Metadata() {
    }

    public Metadata(Instant createdOn, Instant modifiedOn, String createdBy, String creatorName, String modifiedBy, String modifierName, String timeZone) {
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.createdBy = createdBy;
        this.creatorName = creatorName;
        this.modifiedBy = modifiedBy;
        this.modifierName = modifierName;
        this.timeZone = timeZone;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
