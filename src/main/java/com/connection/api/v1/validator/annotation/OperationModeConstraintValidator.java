package com.connection.api.v1.validator.annotation;

import com.connection.api.v1.model.connector.management.OperationModes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class OperationModeConstraintValidator implements ConstraintValidator<ValidateOperationMode, String> {
    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        try {
            for (OperationModes value : OperationModes.values()) {
                if (value.toString().equals(status)) {
                    return true;
                }

            }
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid connection type status, value of status can be one of these: "
                                    + Arrays.asList(OperationModes.values()))
                    .addConstraintViolation();
            return false;
        }
        return false;
    }
}
