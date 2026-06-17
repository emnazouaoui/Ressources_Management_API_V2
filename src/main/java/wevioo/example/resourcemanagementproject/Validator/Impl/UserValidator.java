package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((UserDTO) target, errors);
    }

    // ─── CREATE ───────────────────────────────────────────
    public void validateCreate(UserDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "userDTO");
        validateCommon(dto, errors);

        // password obligatoire à la création
        if (ObjectUtils.isEmpty(dto.getPassword())) {
            errors.rejectValue(ValidationConstants.FIELD_PASSWORD,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PASSWORD,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PASSWORD_INVALID);
        }

        // IDs obligatoires à la création
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
        if (dto.getLevel() == null) {
            errors.rejectValue("level",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    // ─── UPDATE ───────────────────────────────────────────
    public void validateUpdate(UserDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "userDTO");

        // username — validé seulement si fourni
        if (!ObjectUtils.isEmpty(dto.getUsername())) {
            if (dto.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH) {
                errors.rejectValue(ValidationConstants.FIELD_USERNAME,
                        ValidationConstants.ERROR_CODE_INVALID,
                        ValidationConstants.ERROR_USERNAME_MIN_LENGTH);
            } else if (dto.getUsername().length() > ValidationConstants.USERNAME_MAX_LENGTH) {
                errors.rejectValue(ValidationConstants.FIELD_USERNAME,
                        ValidationConstants.ERROR_CODE_INVALID,
                        ValidationConstants.ERROR_USERNAME_MAX_LENGTH);
            }
        }

        // email — validé seulement si fourni
        if (!ObjectUtils.isEmpty(dto.getEmail()) &&
                !ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_EMAIL_INVALID);
        }

        // phone — validé seulement si fourni
        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PHONE,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PHONE_INVALID);
        }

        // password — validé seulement si fourni
        if (!ObjectUtils.isEmpty(dto.getPassword()) &&
                !ValidationConstants.PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PASSWORD,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PASSWORD_INVALID);
        }

        ValidationHelper.validate(errors);
    }

    // ─── Common ───────────────────────────────────────────
    private void validateCommon(UserDTO dto, Errors errors) {

        // username
        if (ObjectUtils.isEmpty(dto.getUsername())) {
            errors.rejectValue(ValidationConstants.FIELD_USERNAME,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (dto.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH) {
            errors.rejectValue(ValidationConstants.FIELD_USERNAME,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_USERNAME_MIN_LENGTH);
        } else if (dto.getUsername().length() > ValidationConstants.USERNAME_MAX_LENGTH) {
            errors.rejectValue(ValidationConstants.FIELD_USERNAME,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_USERNAME_MAX_LENGTH);
        }

        // firstName
        if (ObjectUtils.isEmpty(dto.getFirstName())) {
            errors.rejectValue(ValidationConstants.FIELD_FIRST_NAME,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getFirstName()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_FIRST_NAME,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        // lastName
        if (ObjectUtils.isEmpty(dto.getLastName())) {
            errors.rejectValue(ValidationConstants.FIELD_LAST_NAME,
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getLastName()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_LAST_NAME,
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

        // phone — optionnel
        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PHONE,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PHONE_INVALID);
        }
    }



//    @Override
//    public void validate(Object target, Errors errors) {
//        UserDTO dto = (UserDTO) target;
//        validateUsername(dto, errors);
//        validateFirstName(dto, errors);
//        validateLastName(dto, errors);
//        validateEmail(dto, errors);
//        validatePhone(dto, errors);
//        validatePassword(dto, errors);
//        validateRequiredIds(dto, errors);
//    }
//
//    // ─── Username ──────────────────────────────────────────
//    private void validateUsername(UserDTO dto, Errors errors) {
//        if (ObjectUtils.isEmpty(dto.getUsername())) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_USERNAME,
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA
//            );
//        } else if (dto.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_USERNAME,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_USERNAME_MIN_LENGTH
//            );
//        } else if (dto.getUsername().length() > ValidationConstants.USERNAME_MAX_LENGTH) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_USERNAME,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_USERNAME_MAX_LENGTH
//            );
//        }
//    }
//
//    // ─── First Name ────────────────────────────────────────
//    private void validateFirstName(UserDTO dto, Errors errors) {
//        if (ObjectUtils.isEmpty(dto.getFirstName())) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_FIRST_NAME,
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA
//            );
//        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getFirstName()).matches()) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_FIRST_NAME,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_NAME_INVALID
//            );
//        }
//    }
//
//    // ─── Last Name ─────────────────────────────────────────
//    private void validateLastName(UserDTO dto, Errors errors) {
//        if (ObjectUtils.isEmpty(dto.getLastName())) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_LAST_NAME,
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA
//            );
//        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getLastName()).matches()) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_LAST_NAME,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_NAME_INVALID
//            );
//        }
//    }
//
//    // ─── Email ─────────────────────────────────────────────
//    private void validateEmail(UserDTO dto, Errors errors) {
//        if (ObjectUtils.isEmpty(dto.getEmail())) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_EMAIL,
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA
//            );
//        } else if (!ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_EMAIL,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_EMAIL_INVALID
//            );
//        }
//    }
//
//    // ─── Phone ─────────────────────────────────────────────
//    private void validatePhone(UserDTO dto, Errors errors) {
//        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
//                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_PHONE,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_PHONE_INVALID
//            );
//        }
//    }
//
//    // ─── Password ──────────────────────────────────────────
//    private void validatePassword(UserDTO dto, Errors errors) {
//        if (ObjectUtils.isEmpty(dto.getPassword())) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_PASSWORD,
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA
//            );
//        } else if (!ValidationConstants.PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
//            errors.rejectValue(
//                    ValidationConstants.FIELD_PASSWORD,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_PASSWORD_INVALID
//            );
//        }
//    }
//
//    // ─── Required IDs ──────────────────────────────────────
//    private void validateRequiredIds(UserDTO dto, Errors errors) {
//        if (dto.getRoleId() == null) {
//            errors.rejectValue("roleId",
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
//        }
//        if (dto.getDepartmentId() == null) {
//            errors.rejectValue("departmentId",
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
//        }
//        if (dto.getLevel() == null) {
//            errors.rejectValue("level",
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
//        }
//    }

}
