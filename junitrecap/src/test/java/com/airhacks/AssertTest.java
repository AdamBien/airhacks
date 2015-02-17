package com.airhacks;

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

}
