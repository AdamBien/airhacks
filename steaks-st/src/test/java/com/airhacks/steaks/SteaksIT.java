package com.airhacks.steaks;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class SteaksIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/steaks/resources/steaks");
    }

    @Test
    public void steaks() {
        Response response = this.tut.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonObject json = response.readEntity(JsonObject.class);
        System.out.println("json = " + json);
    }

}
