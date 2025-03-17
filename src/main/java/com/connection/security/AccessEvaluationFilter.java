package com.connection.security;

import com.connection.api.v1.feign.admin.AdminService;
import com.connection.constants.CustomMessages;
import com.connection.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AccessEvaluationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;
    private final ApplicationProperties properties;
    private final AdminService adminService;

    public AccessEvaluationFilter(JwtUtils jwtUtil,
                                  ApplicationProperties properties,
                                  AdminService adminService) {
        this.jwtUtil = jwtUtil;
        this.properties = properties;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPrePermitted(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

//        String jwt = jwtUtil.extractJwtFromHeader(request.getHeader(AUTHORIZATION));
//
//        if (jwt == null) {
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
//            response.getWriter().write(CustomMessages.AUTHENTICATION_TOKEN_MISSING);
//            return;
//        }
//
//        Map<String, String> reqEntity = new HashMap<>(getRequestPayload(request));
//        reqEntity.put("userId",jwtUtil.getUserIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)));
//
//        ResponseEntity<Object> result = adminService.evaluatePermission(reqEntity);
////
//        if (result == null || result.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
//            response.setStatus(403);
//            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
//            response.getWriter().write(CustomMessages.INSUFFICENT_PRIVILEGES_MESSAGE);
//            return;
//        }
//        if (result.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
//            response.setStatus(404);
//            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
//            response.getWriter().write(CustomMessages.RESOURCE_NOT_FOUND);
//            return;
//        }
//        if (result.getStatusCode().value() == 440) {
//            response.setStatus(440);
//            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
//            response.getWriter().write(CustomMessages.SESSION_EXPIRED);
//        }
//
        filterChain.doFilter(request, response);
    }

    private Map<String, String> getRequestPayload(HttpServletRequest request) {
        return Map.of("uri", request.getRequestURI(),
                      "method",request.getMethod());
    }

    private boolean isPrePermitted(String requestPath) {
        for (String uri : properties.getSecurity().getPermittedEndpoints()) {
            if (uri.equals(requestPath)) {
                return true;
            }
        }
        return false;
    }
}
