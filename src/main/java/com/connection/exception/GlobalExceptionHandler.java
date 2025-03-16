package com.connection.exception;

import com.connection.api.v1.model.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;


@Component
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    /****
     I have implemented HandlerExceptionResolver because by default spring
     GlobalExceptionHandler handle only controller and calling methods
     from controller not from the outside of the controller and service
     layer. So to handle Exceptions of whole Application I am
     implementing this class.
     */


    /***
     * Spring first checks @ExceptionHandler methods in @RestControllerAdvice.
     * ✅ If it finds a match (e.g., @ExceptionHandler(ExpiredJwtException.class)), it returns that response.

     * If no @ExceptionHandler method is found, Spring then calls resolveException().
     * ❌ Since resolveException() always sets status 500, it overrides everything.
     */

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
        LOG.error("ApiException: {} ",ex.getResponse());
        ex.printStackTrace();
        return ResponseEntity
                .status(ex.getResponse().getStatusCode())
                .body(ex.getResponse());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        LOG.error("Unhandled Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(500, "An unexpected error occurred", ex.getMessage()));
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwtException(ExpiredJwtException ex) {
        LOG.error("ExpiredJwtException: {}", ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(
                        401, "JWT token has expired", ex.getMessage()
                ));
    }


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.error("Exception in Global Resolver: {}", ex.getMessage(), ex);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"status\":500, \"message\":\"An unexpected error occurred\"}");
            response.getWriter().flush();
        } catch (IOException e) {
            LOG.error("Error writing response: {}", e.getMessage());
        }

        return new ModelAndView();
    }
}