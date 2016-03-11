package com.airhacks;

import java.util.function.Function;
import java.util.function.Predicate;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class LambdaTest {

    @Test
    public void methodReferences() {
        Runnable r = this::beliebig;
        r.run();
    }

    @Test
    public void lambda() {
        Runnable r = () -> System.out.println("hey joe");
        r.run();
    }

    @Test
    public void functions() {
        Function<String, String> function = (t) -> t + "duke";
        String result = function.apply("chief");
        System.out.println("result = " + result);
    }

    @Test
    public void filter() {
        Predicate<String> filter = (a) -> a.equalsIgnoreCase("duke");
        assertTrue(filter.test("duke"));
    }

    public void beliebig() {
        System.out.println("hey joe");
    }

}
