package com.connection.api.v1.validator.annotation;

import com.connection.constants.Connectors;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;


public class ConnectorTypeConstraintValidator implements ConstraintValidator<ValidateConnectorType, String> {


    @Override
    public void initialize(ValidateConnectorType validateConnectorType) {

    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        try {
            for (Connectors.Types connectorTypes : Connectors.Types.values()) {
                if (connectorTypes.toString().equals(type))
                    return true;
            }
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid connector type, value of connector type can be one of these: "
                            + Arrays.asList(Connectors.Types.values())).addConstraintViolation();
            return false;
        }
        return false;
    }
}