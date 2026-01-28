package com.leman.contentmanagementapi.annotation.validator;

import com.leman.contentmanagementapi.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters"
            ).addConstraintViolation();
            return false;
        }

        return password.matches(PASSWORD_PATTERN);
    }

}
