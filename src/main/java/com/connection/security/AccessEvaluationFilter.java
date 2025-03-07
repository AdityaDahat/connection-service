package com.connection.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AccessEvaluationFilter extends OncePerRequestFilter {

    private final ApplicationProperties properties;

    public AccessEvaluationFilter(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPrePermitted(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPrePermitted(String requestPath) {
        for (String uri : properties.getSecurity().getPermittedEndpoints()){
            if (uri.equals(requestPath)) {
                return true;
            }
        }
        return false;
    }
}
