package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class RoleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((RoleDTO) target, errors);
    }

    public void validateCreate(RoleDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "roleDTO");
        validateCommon(dto, errors);
        ValidationHelper.validate(errors);
    }

    public void validateUpdate(RoleDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "roleDTO");

        if (!ObjectUtils.isEmpty(dto.getName()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(RoleDTO dto, Errors errors) {
        if (ObjectUtils.isEmpty(dto.getName())) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }
    }
}
