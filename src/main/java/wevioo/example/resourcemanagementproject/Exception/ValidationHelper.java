package wevioo.example.resourcemanagementproject.Exception;

import org.springframework.validation.BindingResult;
import wevioo.example.resourcemanagementproject.Exception.Custom.ValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

//بدل ما تكرر handleErrors() في كل controller — class مشتركة
public class ValidationHelper {

    public static void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            bindingResult.getFieldErrors()
                    .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            throw new ValidationException(errors);
        }
    }

}
