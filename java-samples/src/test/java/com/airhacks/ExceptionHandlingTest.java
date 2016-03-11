package com.airhacks;

import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 * @author airhacks.com
 */
public class ExceptionHandlingTest {

    private ExceptionThrower cut;

    @Rule
    public ExpectedException none = ExpectedException.none();

    @Before
    public void init() {
        this.cut = new ExceptionThrower();
    }

    @Test
    public void unchecked() {
        none.expect(IllegalStateException.class);
        none.expectMessage(containsString("Lust"));
        this.cut.unstable();
    }

    @Test
    public void uncheckedOldSchool() {
        try {
            this.cut.unstable();
            fail("should fail");
        } catch (Exception ex) {
            assertThat(ex.getMessage(), containsString("Lust"));
        }
    }

    @Test
    public void checked() {
        try {
            this.cut.unable();
            fail("should throw motivation ex");
        } catch (MotivationException ex) {
            //...s
        }
    }

}
