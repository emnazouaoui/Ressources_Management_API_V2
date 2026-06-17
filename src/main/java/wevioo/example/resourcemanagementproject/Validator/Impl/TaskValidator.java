package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class TaskValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return TaskDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((TaskDTO) target, errors);
    }

    public void validateCreate(TaskDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "taskDTO");
        validateCommon(dto, errors);

        if (dto.getStatus() == null) {
            errors.rejectValue("status",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
        if (dto.getPriority() == null) {
            errors.rejectValue("priority",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
        if (dto.getAssignedUserId() == null) {
            errors.rejectValue("assignedUserId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    public void validateUpdate(TaskDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "taskDTO");

        if (!ObjectUtils.isEmpty(dto.getTitle()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getTitle()).matches()) {
            errors.rejectValue("title",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "End date must be after start date");
        }

        if (dto.getEstimatedHours() != null && dto.getEstimatedHours() < 0) {
            errors.rejectValue("estimatedHours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Estimated hours must be positive");
        }

        if (dto.getConsumedHours() != null && dto.getConsumedHours() < 0) {
            errors.rejectValue("consumedHours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Consumed hours must be positive");
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(TaskDTO dto, Errors errors) {
        if (ObjectUtils.isEmpty(dto.getTitle())) {
            errors.rejectValue("title",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getTitle()).matches()) {
            errors.rejectValue("title",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "End date must be after start date");
        }

        if (dto.getEstimatedHours() != null && dto.getEstimatedHours() < 0) {
            errors.rejectValue("estimatedHours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Estimated hours must be positive");
        }

        if (dto.getConsumedHours() != null && dto.getConsumedHours() < 0) {
            errors.rejectValue("consumedHours",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Consumed hours must be positive");
        }
    }

}
