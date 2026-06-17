package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class TechnologyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TechnologyDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((TechnologyDTO) target, errors);
    }

    public void validateCreate(TechnologyDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "technologyDTO");
        validateCommon(dto, errors);
        ValidationHelper.validate(errors);
    }

    public void validateUpdate(TechnologyDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "technologyDTO");

        if (!ObjectUtils.isEmpty(dto.getName()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(TechnologyDTO dto, Errors errors) {
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
