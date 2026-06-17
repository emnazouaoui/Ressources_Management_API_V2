package wevioo.example.resourcemanagementproject.Validator.Impl;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Validator.ValidationConstants;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;


@Component
public class ClientValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientDTO.class.equals(clazz);
    }

    // ← garde pour compatibilité Spring (pas utilisée directement)
    @Override
    public void validate(Object target, Errors errors) {
        validateCommon((ClientDTO) target, errors);
    }

    // ─── CREATE — champs obligatoires ─────────────────────
    public void validateCreate(ClientDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "clientDTO");
        validateCommon(dto, errors);

        // typeClient obligatoire à la création
        if (dto.getTypeClient() == null) {
            errors.rejectValue("typeClient",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        }

        ValidationHelper.validate(errors);
    }

    // ─── UPDATE — seulement les champs fournis ─────────────
    public void validateUpdate(ClientDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "clientDTO");

        // name — validé seulement si fourni
        if (!ObjectUtils.isEmpty(dto.getName()) &&
                !ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
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

        ValidationHelper.validate(errors);
    }

    // ─── Common — règles communes create + update ──────────
    private void validateCommon(ClientDTO dto, Errors errors) {

        // name obligatoire + pattern
        if (ObjectUtils.isEmpty(dto.getName())) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_REQUIRED,
                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
            errors.rejectValue("name",
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_NAME_INVALID);
        }

        // email optionnel
        if (!ObjectUtils.isEmpty(dto.getEmail()) &&
                !ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_EMAIL_INVALID);
        }

        // phone optionnel
        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.rejectValue(ValidationConstants.FIELD_PHONE,
                    ValidationConstants.ERROR_CODE_INVALID,
                    ValidationConstants.ERROR_PHONE_INVALID);
        }
    }


//    @Override
//    public void validate(Object target, Errors errors) {
//        ClientDTO dto = (ClientDTO) target;
//
//        // Name
//        if (ObjectUtils.isEmpty(dto.getName())) {
//            errors.rejectValue("name",
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
//        } else if (!ValidationConstants.NAME_PATTERN.matcher(dto.getName()).matches()) {
//            errors.rejectValue("name",
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_NAME_INVALID);
//        }
//
//        // Email — optionnel mais validé si fourni
//        if (!ObjectUtils.isEmpty(dto.getEmail()) &&
//                !ValidationConstants.EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
//            errors.rejectValue(ValidationConstants.FIELD_EMAIL,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_EMAIL_INVALID);
//        }
//
//        // Phone — optionnel mais validé si fourni
//        if (!ObjectUtils.isEmpty(dto.getPhone()) &&
//                !ValidationConstants.PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
//            errors.rejectValue(ValidationConstants.FIELD_PHONE,
//                    ValidationConstants.ERROR_CODE_INVALID,
//                    ValidationConstants.ERROR_PHONE_INVALID);
//        }
//
//        // TypeClient
//        if (dto.getTypeClient() == null) {
//            errors.rejectValue("typeClient",
//                    ValidationConstants.ERROR_CODE_REQUIRED,
//                    ValidationConstants.ERROR_MISSING_REQUIRED_DATA);
//        }
//    }

}
