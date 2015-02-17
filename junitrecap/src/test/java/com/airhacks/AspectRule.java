package com.airhacks;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *
 * @author airhacks.com
 */
public class AspectRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                System.out.println("AspectRule::before");
                base.evaluate();
                System.out.println("AspectRule::after");
            }
        };
    }

}
