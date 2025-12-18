package airhacks.greetings.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.greetings.control.Claude;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Blocking
@Path("hello")
public class GreetingResource {

    @Inject
    Claude claude;

    @Inject
    @ConfigProperty(name="user.message",defaultValue = "Greet developer: %s")
    String userMessage;

    @GET
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") String name) {
        var message = userMessage.formatted(name);
        System.out.println("calling claude");
        var response = this.claude.chat(message);
        System.out.println("claude called, returning "  + response);
        return response;
    }
}
