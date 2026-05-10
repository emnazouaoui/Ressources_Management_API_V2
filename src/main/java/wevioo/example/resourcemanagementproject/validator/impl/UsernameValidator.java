package wevioo.example.resourcemanagementproject.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wevioo.example.resourcemanagementproject.validator.annotation.ValidUsername;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String>{

    private static final java.util.regex.Pattern USERNAME_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");

    //✅ john_doe ✅ user.123 ✅ dev-team
    //❌ jo (trop court) ❌ john doe (espace interdit) ❌ user@name (@ interdit)
    //Username must be 3-50 chars (letters, numbers, dots, underscores, hyphens only

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return USERNAME_PATTERN.matcher(value).matches();
    }
}
