package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

import java.util.regex.Pattern;

@Component
public class ProjectTimeLineValidator implements Validator {

    private static final Pattern VERSION_PATTERN = Pattern.compile(
            "^[0-9]+\\.[0-9]+(\\.[0-9]+)?$"
    );

    @Override
    public boolean supports(Class<?> clazz) {
        return ProjectTimeLineDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProjectTimeLineDTO dto = (ProjectTimeLineDTO) target;

        if (ObjectUtils.isEmpty(dto.getTitle())) {
            errors.rejectValue("title",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getTitle()).matches()) {
            errors.rejectValue("title",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        if (dto.getProjectId() == null) {
            errors.rejectValue("projectId",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        if (dto.getProgressPercent() != null &&
                (dto.getProgressPercent() < 0 || dto.getProgressPercent() > 100)) {
            errors.rejectValue("progressPercent",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Progress must be between 0 and 100");
        }

        if (!ObjectUtils.isEmpty(dto.getVersion()) &&
                !VERSION_PATTERN.matcher(dto.getVersion()).matches()) {
            errors.rejectValue("version",
                    ValidationConstants.ERROR_CODE_INVALID,
                    "Version format must be like 1.0 or 1.0.0");
        }

        if (dto.getType() == null) {
            errors.rejectValue("type",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
    }

}
