package com.airhacks.mutation;

import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CalculatorTest {

    @Test
    public void multiply() {
        int a = 2;
        int b = 3;
        Calculator cut = new Calculator(a, b);
        cut.multiply();
        Integer result = cut.getResult();
        /*
        assertNotNull(result);
        assertThat(result, is(a * b));
         */
    }

}
