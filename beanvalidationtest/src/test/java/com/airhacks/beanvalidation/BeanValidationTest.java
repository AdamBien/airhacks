package com.airhacks.beanvalidation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class BeanValidationTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @Before
    public void init() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = this.validatorFactory.getValidator();
    }

    @Test
    public void unsufficientAttendees() {
        Workshop workshop = new Workshop("bootstrap", 2);
        Set<ConstraintViolation<Workshop>> violations = this.validator.validate(workshop);
        assertThat(violations.size(), is(1));
    }

    @Test
    public void nameTooShortAndNotSufficientRegistrations() {
        Workshop workshop = new Workshop("b", 2);
        Set<ConstraintViolation<Workshop>> violations = this.validator.validate(workshop);
        assertThat(violations.size(), is(2));

    }

}
