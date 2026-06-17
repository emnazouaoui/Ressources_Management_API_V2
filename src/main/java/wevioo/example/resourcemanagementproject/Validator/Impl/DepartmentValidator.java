package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class DepartmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((DepartmentDTO) target, errors);
    }

    public void validateCreate(DepartmentDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "departmentDTO");
        validateCommon(dto, errors);
        ValidationHelper.validate(errors);
    }

    public void validateUpdate(DepartmentDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "departmentDTO");

        if (!ObjectUtils.isEmpty(dto.getName()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (!ObjectUtils.isEmpty(dto.getDescription()) &&
                dto.getDescription().length() > 500) {
            errors.rejectValue("description",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Description must not exceed 500 characters");
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(DepartmentDTO dto, Errors errors) {
        if (ObjectUtils.isEmpty(dto.getName())) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (!ObjectUtils.isEmpty(dto.getDescription()) &&
                dto.getDescription().length() > 500) {
            errors.rejectValue("description",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Description must not exceed 500 characters");
        }
    }
}
