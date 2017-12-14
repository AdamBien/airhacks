/*
 */
package com.airhacks.jaxrs;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;

public class ReactiveClientTest {

    private WebTarget tut;
    private Client client;

    @Before
    public void initClient() {
        ExecutorService threadPool = Executors.newFixedThreadPool(1, (runnable) -> {
            System.out.println("-- requesting thread with " + runnable);
            return new Thread(runnable, "--custom--made--");
        }
        );
        this.client = ClientBuilder.
                newBuilder().
                executorService(threadPool).
                build();
        this.tut = this.client.target("http://airhacks.com");
    }


    @Test
    public void reactive() throws InterruptedException {
        CompletionStage<Response> stage = this.tut.
                request().
                rx().
                get();
        stage.
                thenApply(req -> req.readEntity(String.class)).
                thenAccept(System.out::println);
        Thread.sleep(500);
    }

}
