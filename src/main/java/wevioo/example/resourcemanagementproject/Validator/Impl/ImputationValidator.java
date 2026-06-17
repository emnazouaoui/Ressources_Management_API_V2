package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class ImputationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ImputationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((ImputationDTO) target, errors);
    }

    public void validateCreate(ImputationDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "imputationDTO");
        validateCommon(dto, errors);

        if (dto.getTaskId() == null) {
            errors.rejectValue("taskId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
        if (dto.getUserId() == null) {
            errors.rejectValue("userId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    public void validateUpdate(ImputationDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "imputationDTO");

        if (dto.getHours() != null) {
            if (dto.getHours() < 0.1) {
                errors.rejectValue("hours",
                        ValidationConstants.ERROR_CODE_INVALID,
                        "Hours must be at least 0.1");
            } else if (dto.getHours() > 24.0) {
                errors.rejectValue("hours",
                        ValidationConstants.ERROR_CODE_INVALID,
                        "Hours cannot exceed 24");
            }
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(ImputationDTO dto, Errors errors) {
        if (dto.getDate() == null) {
            errors.rejectValue("date",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getHours() == null) {
            errors.rejectValue("hours",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (dto.getHours() < 0.1) {
            errors.rejectValue("hours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Hours must be at least 0.1");
        } else if (dto.getHours() > 24.0) {
            errors.rejectValue("hours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Hours cannot exceed 24");
        }
    }
}
