package com.connection.security.configuration;

import com.connection.security.ApplicationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PropertiesTester implements CommandLineRunner {

    private final ApplicationProperties applicationProperties;

    public PropertiesTester(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void run(String... args) {
        System.out.println("Context Path: " + applicationProperties.getContextPath());
        System.out.println("API Domain: " + applicationProperties.getApiDomain());
        System.out.println("UI Domain: " + applicationProperties.getUiDomain());
        System.out.println("Allowed Methods: " + Arrays.toString(applicationProperties.getSecurity().getAllowedMethods()));
        System.out.println("Security Allowed Origins: " + String.join(", ", applicationProperties.getSecurity().getAllowedOrigins()));
    }
}
