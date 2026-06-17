package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class ProjectValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return ProjectDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((ProjectDTO) target, errors);
    }

    public void validateCreate(ProjectDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "projectDTO");
        validateCommon(dto, errors);

        if (dto.getProjectManagerId() == null) {
            errors.rejectValue("projectManagerId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
        if (dto.getClientId() == null) {
            errors.rejectValue("clientId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    public void validateUpdate(ProjectDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "projectDTO");

        if (!ObjectUtils.isEmpty(dto.getName()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "End date must be after start date");
        }

        if (dto.getProgressPercent() != null &&
                (dto.getProgressPercent() < 0 || dto.getProgressPercent() > 100)) {
            errors.rejectValue("progressPercent",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Progress must be between 0 and 100");
        }

        ValidationHelper.validate(errors);
    }

    private void validateCommon(ProjectDTO dto, Errors errors) {
        if (ObjectUtils.isEmpty(dto.getName())) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (ObjectUtils.isEmpty(dto.getStatus())) {
            errors.rejectValue("status",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "End date must be after start date");
        }

        if (dto.getProgressPercent() != null &&
                (dto.getProgressPercent() < 0 || dto.getProgressPercent() > 100)) {
            errors.rejectValue("progressPercent",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Progress must be between 0 and 100");
        }
    }
}
