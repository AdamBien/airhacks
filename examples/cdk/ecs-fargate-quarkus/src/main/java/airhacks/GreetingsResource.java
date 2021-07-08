package airhacks;

import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/greetings")
public class GreetingsResource {
    static Logger LOG = Logger.getLogger(GreetingsResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }


    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response notificationEndpoint(@HeaderParam("x-amz-sns-message-type") String messageType, String message){
                LOG.info("messageType " + messageType + " payload " + message);
        return Response.ok().build();
    }
}