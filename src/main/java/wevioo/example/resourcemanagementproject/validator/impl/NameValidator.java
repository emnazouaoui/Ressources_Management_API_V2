package wevioo.example.resourcemanagementproject.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wevioo.example.resourcemanagementproject.validator.annotation.ValidName;

public class NameValidator implements ConstraintValidator<ValidName, String>{

    private static final java.util.regex.Pattern NAME_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s'\\-_.]{1,255}$");

    //✅ Mohamed Ali ✅ Société Générale ✅ O'Brien
    //❌ Name@123 (@ interdit) ❌ `` (vide interdit)
    //Name can only contain letters, numbers, spaces, hyphens and apostrophes

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return NAME_PATTERN.matcher(value).matches();
    }
}
