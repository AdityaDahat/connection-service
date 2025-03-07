package com.connection.exception;

import com.connection.api.v1.model.response.ApiResponse;

public class ApiException extends RuntimeException {
    private final ApiResponse<?> response;

    public ApiException(int statusCode, String message, String reason, Object additionalInfo) {
        super(message);
        this.response = new ApiResponse<>(statusCode, message, reason, additionalInfo);
    }

    public ApiException(int statusCode, String message, String reason) {
        super(message);
        this.response = new ApiResponse<>(statusCode, message, reason);
    }

    public ApiException(int statusCode, String message) {
        super(message);
        this.response = new ApiResponse<>(statusCode, message);
    }

    public <T> ApiException(ApiResponse<T> apiResponse) {
        super(apiResponse.getMessage());
        this.response = apiResponse;
    }

    public ApiResponse<?> getResponse() {
        return response;
    }

}
