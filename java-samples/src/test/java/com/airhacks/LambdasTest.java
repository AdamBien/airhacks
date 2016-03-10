package com.airhacks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class LambdasTest {

    @Test
    public void runnable() {
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.submit(this::action);
    }

    public void action() {
        System.out.println("am schlag anschlag");
    }

    @Test
    public void callable() throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        // see https://github.com/kriskowal/q
        Callable<String> callable = this::status;
        Future<String> future = service.submit(callable);
        String result = future.get();
        System.out.println("result: " + result);
    }

    @Test
    public void parallelization() throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<Long>> asyncs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            asyncs.add(service.submit(this::slow));
        }
        for (Future<Long> async : asyncs) {
            System.out.println("async = " + async.get());
        }

    }

    public long slow() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(LambdasTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return System.currentTimeMillis();
    }

    public String status() {
        return "exploded";
    }

    public void audit(String msg) {
        System.out.println(" " + msg);
    }

}
