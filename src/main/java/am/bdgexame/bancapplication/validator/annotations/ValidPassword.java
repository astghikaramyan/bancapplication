package am.bdgexame.bancapplication.validator.annotations;

import am.bdgexame.bancapplication.validator.constraints.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must be equal to or greater than 8 characters and less than 16 characters and contains minimum 1 special, 1 uppercase and 1 digit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
