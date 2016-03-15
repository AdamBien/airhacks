package com.airhacks.damages.boundary;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DamagesResourceIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/hello/resources/damages/");
    }

    @Test
    public void crud() {
        String expected = "42";
        Response response = this.tut.path(expected).request().get();
        assertThat(response.getStatus(), is(200));
        String damage = response.readEntity(String.class);
        assertThat(damage, startsWith(expected));
        System.out.println("damage = " + damage);
    }

}
