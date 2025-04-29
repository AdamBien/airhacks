package valantic.greetiers.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import valantic.persistence.control.Persistence;

@Path("/hello")
public class GreetingResource {

    @Inject
    @ConfigProperty(name = "greetings.message",defaultValue = "no clouds")
    String message;

    @Inject
    Persistence persistence;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        persistence.save("greet", message);
        return "Hello from Quarkus REST 3 " + message;
    }
}
