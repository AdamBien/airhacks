package airhacks.a2a.greeter;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class GreeterResource {

    static final Logger LOGGER = System.getLogger(GreeterResource.class.getName());

    @Inject
    GreeterAgentClient client;

    @GET
    public String hello() {
        LOGGER.log(Level.INFO, "GET /hello invoked");
        this.client.sendMessage("hello, duke");
        return "+";
    }
}
