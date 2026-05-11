package wevioo.example.resourcemanagementproject.Validator.Annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wevioo.example.resourcemanagementproject.Validator.Impl.UsernameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "Username must be 3-50 chars (letters, numbers, dots, underscores, hyphens only)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
