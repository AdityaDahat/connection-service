package com.connection.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "databyte")
public class ApplicationProperties {

    private SecurityProperties security;

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public static class SecurityProperties{
            private String apiMatcher;
            private String abstractPath;
            private String[] permittedEndpoints;
            private String[] abstractEndpoints;
            private String[] allowedOrigins;
            private String[] allowedMethods;
            private String[] allowedHeaders;

        public String getApiMatcher() {
            return apiMatcher;
        }

        public void setApiMatcher(String apiMatcher) {
            this.apiMatcher = apiMatcher;
        }

        public String getAbstractPath() {
            return abstractPath;
        }

        public void setAbstractPath(String abstractPath) {
            this.abstractPath = abstractPath;
        }

        public String[] getPermittedEndpoints() {
            return permittedEndpoints;
        }

        public void setPermittedEndpoints(String[] permittedEndpoints) {
            this.permittedEndpoints = permittedEndpoints;
        }

        public String[] getAbstractEndpoints() {
            return abstractEndpoints;
        }

        public void setAbstractEndpoints(String[] abstractEndpoints) {
            this.abstractEndpoints = abstractEndpoints;
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
    }
}

