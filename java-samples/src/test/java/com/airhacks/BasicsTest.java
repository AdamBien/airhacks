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

    @Test
    public void withReferences() {
        Runnable r = this::tnt;
        new Thread(r).start();
    }

    //Thread is not allowed in Java EE
    @Test
    public void forManuel() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                tnt();
            }
        };
        new Thread(r).start();
    }

    void tnt() {
        System.out.println("TNT action");
    }

    void hello() {
        System.out.println("Hey joe");
    }

    static void goodbye() {
        System.out.println("no this");
    }

}
