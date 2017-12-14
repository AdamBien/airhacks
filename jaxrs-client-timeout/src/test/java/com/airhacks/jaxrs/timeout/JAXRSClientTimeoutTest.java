/*
 */
package com.airhacks.jaxrs.timeout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author airhacks.com
 */
public class JAXRSClientTimeoutTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void connectTimeout() {
        expected.expect(ProcessingException.class);
        Client client = ClientBuilder.
                newBuilder().
                connectTimeout(1, TimeUnit.MILLISECONDS).
                build();
        //no one listens here
        client.target("http://127.0.0.1").
                request().
                get();
    }

    @Test
    public void readTimeout() {
        expected.expect(ProcessingException.class);
        Client client = ClientBuilder.
                newBuilder().
                readTimeout(1, TimeUnit.MILLISECONDS).
                build();
        //airhacks.com is too slow to answer in 1ms
        client.target("http://www.adam-bien.com/roller/abien/entry/setting_timeout_for_the_jax").
                request().
                get();
    }

    @Test
    public void executor() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Client client = ClientBuilder.
                newBuilder().executorService(executor).
                build();

        Future<Response> result = client.target("http://localhost:8080").
                request().
                async().
                get();
        System.out.println("-- " + result.get());

    }

}
