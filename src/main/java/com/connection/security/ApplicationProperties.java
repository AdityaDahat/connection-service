package com.connection.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "databyte")
public class ApplicationProperties {

    private SecurityProperties security = new SecurityProperties();
    private DataByteProperties dataByte = new DataByteProperties();

    // ✅ GETTERS & SETTERS
    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public DataByteProperties getDataByte() {
        return dataByte;
    }

    public void setDataByte(DataByteProperties dataByte) {
        this.dataByte = dataByte;
    }

    // ✅ Security Properties Class
    public static class SecurityProperties {
        private String[] apiMatcher;
        private String[] abstractEndpoints;
        private String[] permittedEndpoints;
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;

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
    }

    // ✅ DataByte Properties Class
    public static class DataByteProperties {
        private String contextPath;
        private String apiDomain;
        private String uiDomain;

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
    }
}
