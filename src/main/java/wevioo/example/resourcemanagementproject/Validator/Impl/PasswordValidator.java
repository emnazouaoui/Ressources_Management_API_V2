package wevioo.example.resourcemanagementproject.Validator.Impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wevioo.example.resourcemanagementproject.Validator.Annotation.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{

    private static final java.util.regex.Pattern PASSWORD_PATTERN =
            java.util.regex.Pattern.compile(
                    "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            );

    //Lookahead (?=...) = condition qui vérifie sans consommer les caractères
    //
    //✅ Password1@ ✅ Secure#99
    //❌ password ❌ PASSWORD1 ❌ Pass1
    //Password must be at least 8 chars with 1 uppercase, 1 lowercase, 1 digit and 1 special character

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return PASSWORD_PATTERN.matcher(value).matches();
    }
}
