package com.airhacks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void crawler() {
        List<String> uris = Arrays.asList("http://www.google.de", "http://www.pc-agrar.de");
        uris.forEach(System.out::println);
        List<String> contents = uris.parallelStream().
                map(this::fetchContent).
                collect(Collectors.toList());
        contents.forEach(System.out::println);
    }

    public String fetchContent(String uri) {
        return this.client.target(uri).request().get(String.class);
    }

}
