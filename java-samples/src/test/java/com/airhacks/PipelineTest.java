package com.airhacks;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class PipelineTest {

    @Test
    public void pipeline() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        supplyAsync(this::slow, service).
                thenApply(this::process).
                thenAccept(this::log);
    }

    public String slow() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String process(String input) {
        return "# " + input;
    }

    public void log(String msg) {
        System.out.println("msg = " + msg);
    }

}
