package com.airhacks.workshops;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class WorkshopsResourceIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        String host = System.getProperty("host", "localhost");
        String port = System.getProperty("port", "8080");
        this.tut = this.client.target("http://{host}:{port}/javaeetesting/resources/workshops").
                resolveTemplate("host", host).
                resolveTemplate("port", port);
    }

    @Test
    public void getWorkshops() {
        Response response = this.tut.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertNotNull(payload);
        assertThat(payload.keySet(), hasItem("workshops"));

    }

}
