package com.connection.api.v1.payload.connector.management;


import com.connection.api.v1.model.connector.management.ConnectionTypeDetails;
import com.connection.api.v1.model.connector.management.OperationModes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.connection.api.v1.validator.annotation.ValidateOperationMode;

public class ConnectionTypeUpdatePayload {

	private String id;

	@NotNull(message = "name cannot be null")
	@NotEmpty(message = "name cannot be empty")
	private String name;

	@NotNull(message = "shortName cannot be null")
	@NotEmpty(message = "shortName cannot be empty")
	private String shortName;

	@Valid
	@NotNull
	private ConnectionTypeDetails typeDetails;

	private Integer sequenceIndex;
	private Float version = Float.valueOf(0);
	private boolean skipFlowScripts;
	private boolean isGlobalConnectionType;
	private boolean isPrivate;

	@ValidateOperationMode
	private String status = OperationModes.COMING_SOON.toString();

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

	public ConnectionTypeDetails getTypeDetails() {
		return typeDetails;
	}

	public void setTypeDetails(ConnectionTypeDetails typeDetails) {
		this.typeDetails = typeDetails;
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

	public void setGlobalConnectionType(boolean isGlobalConnectionType) {
		this.isGlobalConnectionType = isGlobalConnectionType;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
