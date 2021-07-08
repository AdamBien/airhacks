package airhacks;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
//mvn clean install -Dnative -Dnative-image.docker-build=true -Dquarkus.native.container-runtime=docker
//sam deploy-t target/sam.native.yaml -g
//curl -w 'Total: %{time_total}s\n' https://b2n5571nbk.execute-api.eu-central-1.amazonaws.com/Prod/greetings
@Path("greetings")
public class GreetingResource {

    static Logger LOG = Logger.getLogger(GreetingResource.class.getName());

    @Inject
    @ConfigProperty(name = "greeting", defaultValue = "hello, lambda")
    String greetings;

    @Inject
    Greeter greeter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String greet() {
        LOG.info("returning " + greetings);
        return this.greetings + " " +LocalDateTime.now() + this.greeter.helloFromClouds();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void greet(String message) {
        LOG.info("Greeting received: " + message);
    }
}
