package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import wevioo.example.resourcemanagementproject.DTO.ForgetPasswordRequest;
import wevioo.example.resourcemanagementproject.DTO.RegisterRequest;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;

@Component
public class AuthValidator {

    // ─── REGISTER ───────────────────────────────────────────
    public void validateRegister(RegisterRequest dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "registerRequest");

        // username
        if (ObjectUtils.isEmpty(dto.getUsername())) {
            errors.rejectValue("username",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (dto.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH) {
            errors.rejectValue("username",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_USERNAME_MIN_LENGTH);
        }

        // firstName
        if (ObjectUtils.isEmpty(dto.getFirstName())) {
            errors.rejectValue("firstName",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getFirstName()).matches()) {
            errors.rejectValue("firstName",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        // lastName
        if (ObjectUtils.isEmpty(dto.getLastName())) {
            errors.rejectValue("lastName",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getLastName()).matches()) {
            errors.rejectValue("lastName",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        // email
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_EMAIL_INVALID);
        }

        // password — ✅ appel au pattern existant
        validatePasswordField(dto.getPassword(), errors, "password");

        // phone — optionnel
        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PHONE,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PHONE_INVALID);
        }

        // roleId + departmentId obligatoires
        if (dto.getRoleId() == null) {
            errors.rejectValue("roleId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
        if (dto.getDepartmentId() == null) {
            errors.rejectValue("departmentId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    // ─── FORGET PASSWORD ────────────────────────────────────
    public void validateForgetPassword(ForgetPasswordRequest dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "forgetPasswordRequest");

        // email
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_EMAIL_INVALID);
        }

        // newPassword — ✅ même pattern que register
        validatePasswordField(dto.getNewPassword(), errors, "newPassword");

        // confirmPassword
        if (ObjectUtils.isEmpty(dto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!dto.getConfirmPassword().equals(dto.getNewPassword())) {
            errors.rejectValue("confirmPassword",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Passwords do not match");
        }

        ValidationHelper.validate(errors);
    }

    // ─── Helper réutilisable — pattern password ────────────
    private void validatePasswordField(String password, Errors errors, String fieldName) {
        if (ObjectUtils.isEmpty(password)) {
            errors.rejectValue(fieldName,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.PASSWORD_PATTERN.matcher(password).matches()) {
            errors.rejectValue(fieldName,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PASSWORD_INVALID);
        }
    }

}
