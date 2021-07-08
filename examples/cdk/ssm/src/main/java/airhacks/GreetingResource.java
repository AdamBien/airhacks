package airhacks;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;


@Path("/hello")
public class GreetingResource {

    @Inject
    SsmClient ssm;

    @ConfigProperty(name = "ssm.parameter",defaultValue = "message")
    String parameterPath;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        var request = GetParameterRequest.builder().name("message").build();
        return ssm.getParameter(request).parameter().value();
    }
}