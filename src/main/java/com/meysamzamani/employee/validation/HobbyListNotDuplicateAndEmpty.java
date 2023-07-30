package com.meysamzamani.employee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HobbyListValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HobbyListNotDuplicateAndEmpty {
    String message() default "hobbies list must not be contain duplicate or empty value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
