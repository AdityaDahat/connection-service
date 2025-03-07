package com.connection.api.v1.model.response;

import com.connection.api.v1.model.connector.management.ConnectionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.Page;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private String message;
    private int statusCode;
    private String reason;
    private Object additionalInfo;
    private Instant timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(T data, int statusCode) {
        super();
        this.data = data;
        this.statusCode = statusCode;
        this.timestamp = Instant.now();
    }

    public ApiResponse(int statusCode, String message, String reason, Object additionalInfo) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.reason = reason;
        this.additionalInfo = additionalInfo;
        this.timestamp = Instant.now();
    }

    public ApiResponse(int statusCode, String message, String reason) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.reason = reason;
        this.timestamp = Instant.now();
    }

    public ApiResponse(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public ApiResponse(T data) {
        super();
        this.data = data;
        this.timestamp = Instant.now();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().registerModules(new JavaTimeModule()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }

}
