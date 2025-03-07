package com.connection.api.v1.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = OperationModeConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RUNTIME)
public @interface ValidateOperationMode {

    String message() default "Invalid connection type status.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
