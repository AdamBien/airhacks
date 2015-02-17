package com.airhacks;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AssertTest {

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

}
