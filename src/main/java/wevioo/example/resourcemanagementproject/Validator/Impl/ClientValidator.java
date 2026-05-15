package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;

@Component
public class ClientValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClientDTO dto = (ClientDTO) target;

        // Name
        if (ObjectUtils.isEmpty(dto.getName())) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        // Email — optionnel mais validé si fourni
        if (!ObjectUtils.isEmpty(dto.getEmail()) &&
                !ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_EMAIL_INVALID);
        }

        // Phone — optionnel mais validé si fourni
        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PHONE,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PHONE_INVALID);
        }

        // TypeClient
        if (dto.getTypeClient() == null) {
            errors.rejectValue("typeClient",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }
    }

}
