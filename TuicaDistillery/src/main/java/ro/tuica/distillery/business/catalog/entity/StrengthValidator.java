package ro.tuica.distillery.business.catalog.entity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author airhacks.com
 */
public class StrengthValidator implements ConstraintValidator<Strength, Tuica> {

    @Override
    public void initialize(Strength constraintAnnotation) {

    }

    @Override
    public boolean isValid(Tuica value, ConstraintValidatorContext context) {
        return value.isValid();
    }
}
