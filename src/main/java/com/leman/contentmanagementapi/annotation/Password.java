package com.leman.contentmanagementapi.annotation;

import com.leman.contentmanagementapi.annotation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "must contain at least one digit, lowercase, uppercase, and special character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
