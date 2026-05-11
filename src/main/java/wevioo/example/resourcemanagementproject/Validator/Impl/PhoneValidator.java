package wevioo.example.resourcemanagementproject.Validator.Impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import wevioo.example.resourcemanagementproject.Validator.Annotation.ValidPhone;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String>{

    private static final java.util.regex.Pattern PHONE_PATTERN =
            java.util.regex.Pattern.compile("^[+]?[0-9]{8,15}$");

    //✅ 12345678 ✅ +21612345678
    //❌ 123 (trop court) ❌ +216abc (lettres interdites)
    // phone number (8-15 digits, optional + prefix)

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        return PHONE_PATTERN.matcher(value).matches();
    }
}
