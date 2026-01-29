package ro.tuica.distillery.business.catalog.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 * @author airhacks.com
 */
@Documented
@Constraint(validatedBy = StrengthValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Strength {

    String message() default "{ro.tuica.distillery.business.catalog.entity.Strength}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
