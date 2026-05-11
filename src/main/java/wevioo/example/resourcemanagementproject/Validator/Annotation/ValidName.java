package wevioo.example.resourcemanagementproject.Validator.Annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wevioo.example.resourcemanagementproject.Validator.Impl.NameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String message() default "Name can only contain letters, numbers, spaces, hyphens and apostrophes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
