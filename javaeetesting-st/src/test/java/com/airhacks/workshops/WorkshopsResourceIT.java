package com.airhacks.workshops;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import static junit.framework.Assert.assertNotNull;
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
        this.tut = this.client.target("http://localhost:8080/javaeetesting/resources/workshops");
    }

    @Test
    public void getWorkshops() {
        JsonObject result = this.tut.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        assertNotNull(result);
        System.out.println("result = " + result);
    }

}
