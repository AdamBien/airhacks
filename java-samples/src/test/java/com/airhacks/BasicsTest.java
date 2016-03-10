package com.airhacks;

import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class BasicsTest {

    @Test
    public void intf() {
        Action a = ActionFactory.create();
        a.doSomething();
    }

}
