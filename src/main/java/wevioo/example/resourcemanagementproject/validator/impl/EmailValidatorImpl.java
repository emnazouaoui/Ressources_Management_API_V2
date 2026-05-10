package wevioo.example.resourcemanagementproject.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wevioo.example.resourcemanagementproject.validator.annotation.ValidEmail;

public class EmailValidatorImpl implements ConstraintValidator<ValidEmail, String>{

    private static final java.util.regex.Pattern EMAIL_PATTERN =
            java.util.regex.Pattern.compile(
                    "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
            );
    //✅ user@gmail.com ✅ test.user@company.tn
    //❌ user@ ❌ @gmail.com ❌ user@gmail
    //email format
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
