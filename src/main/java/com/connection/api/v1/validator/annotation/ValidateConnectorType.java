package com.connection.api.v1.validator.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConnectorTypeConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidateConnectorType {
    String message() default "Invalid connector type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
