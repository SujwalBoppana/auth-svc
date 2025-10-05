package dev.tbyte.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(obj)
                .getPropertyValue("password");
        Object confirmPasswordValue = new BeanWrapperImpl(obj)
                .getPropertyValue("confirmPassword");

        if (passwordValue != null) {
            return passwordValue.equals(confirmPasswordValue);
        } else {
            return confirmPasswordValue == null;
        }
    }
}