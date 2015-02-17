package com.airhacks;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author airhacks.com
 */
public class AssertTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public AspectRule custom = new AspectRule();

    @Before
    public void before() {
        System.out.println("Before");
    }

    @Test
    public void stringAssert() {
        assertThat("javaee",
                allOf(containsString("java"),
                        containsString("ee")));
        assertThat("javaee", allOf(startsWith("java"), endsWith("ee")));
        assertThat("javaee", not(allOf(startsWith("scalae"),
                endsWith("groovy"))));
    }

    @Test
    public void collections() {
        List<String> coolStuff = Arrays.asList("java", "jee");
        assertThat(coolStuff, everyItem(containsString("j")));
        assertThat(coolStuff, hasItem("java"));
    }

    @Test(expected = IllegalStateException.class)
    public void expectedException() {
        throw new IllegalStateException("Exception");
    }

    @Test
    public void morePowerfulException() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage(startsWith("With"));
        throw new IllegalStateException("With exception it looks fine");
    }

    @Test
    public void ruleTest() {
        System.out.println("ruleTest");
    }

    @Test
    public void fancyLogic() {
        Assume.assumeFalse(true);
    }

    @After
    public void after() {
        System.out.println("After");
    }

}
