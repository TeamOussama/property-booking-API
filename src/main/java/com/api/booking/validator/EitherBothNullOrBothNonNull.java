package com.api.booking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EitherBothNullOrBothNonNullValidator.class})
public @interface EitherBothNullOrBothNonNull {
    String message() default "Both startDate and endDate must be either null or non-null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
