package com.connection.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "databyte")
public class ApplicationProperties {

    private String contextPath;
    private String apiDomain;
    private String uiDomain;

    @NestedConfigurationProperty
    private SecurityProperties security = new SecurityProperties();

    // GETTERS & SETTERS
    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getUiDomain() {
        return uiDomain;
    }

    public void setUiDomain(String uiDomain) {
        this.uiDomain = uiDomain;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public static class SecurityProperties {
        private String[] apiMatcher;
        private String[] abstractEndpoints;
        private String[] permittedEndpoints;
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
        private boolean allowCredentials;

        // GETTERS & SETTERS
        public String[] getApiMatcher() {
            return apiMatcher;
        }

        public void setApiMatcher(String[] apiMatcher) {
            this.apiMatcher = apiMatcher;
        }

        public String[] getAbstractEndpoints() {
            return abstractEndpoints;
        }

        public void setAbstractEndpoints(String[] abstractEndpoints) {
            this.abstractEndpoints = abstractEndpoints;
        }

        public String[] getPermittedEndpoints() {
            return permittedEndpoints;
        }

        public void setPermittedEndpoints(String[] permittedEndpoints) {
            this.permittedEndpoints = permittedEndpoints;
        }

        public String[] getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String[] allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String[] allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String[] getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String[] allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
    }
}
