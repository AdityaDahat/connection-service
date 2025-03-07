package com.connection.api.v1.payload.connector.management;

import jakarta.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

public class ConnectionTypeCategoryUpdatePayload {

	@NotEmpty(message = "Category id is required.")
	private String id;

	@NotEmpty(message = "Category name is required.")
	private String name;

	@NotEmpty(message = "Category type is required.")
	private String type;

	private Map<String, Object> additionalInfo = new HashMap<>();

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

}
