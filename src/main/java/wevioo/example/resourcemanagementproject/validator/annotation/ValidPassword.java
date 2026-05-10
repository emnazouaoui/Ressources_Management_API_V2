package wevioo.example.resourcemanagementproject.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import wevioo.example.resourcemanagementproject.validator.impl.PasswordValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be at least 8 chars with 1 uppercase, 1 lowercase, 1 digit and 1 special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
