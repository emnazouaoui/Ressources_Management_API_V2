package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class LeaveRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LeaveRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LeaveRequestDTO dto = (LeaveRequestDTO) target;

        if (dto.getType() == null) {
            errors.rejectValue("type",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getStartDate() == null) {
            errors.rejectValue("startDate",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getEndDate() == null) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        // startDate doit être avant endDate
        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue("endDate",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "End date must be after start date");
        }

        if (dto.getUserId() == null) {
            errors.rejectValue("userId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getProjectManagerId() == null) {
            errors.rejectValue("projectManagerId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
    }

}
