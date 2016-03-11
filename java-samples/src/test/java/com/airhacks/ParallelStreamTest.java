package com.airhacks;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.junit.Before;

/**
 *
 * @author airhacks.com
 */
public class ParallelStreamTest {

    private Client client;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    public String fetchContent(String uri) {
        return this.client.target(uri).request().get(String.class);
    }

}
