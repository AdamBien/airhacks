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

    @Test
    public void reference() {
        Action a = this::hello;
        a.doSomething();

        Action b = BasicsTest::goodbye;
        b.doSomething();
    }

    @Test
    public void threads() {
        Thread t = new Thread(new TNTRunnable());
        t.start();
    }

    void hello() {
        System.out.println("Hey joe");
    }

    static void goodbye() {
        System.out.println("no this");
    }

}
