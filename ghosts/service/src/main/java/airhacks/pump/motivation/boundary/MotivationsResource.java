package airhacks.pump.motivation.boundary;

import airhacks.pump.motivation.control.Arnold;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("motivations")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MotivationsResource {

    @Inject
    Arnold arnold;

    @GET
    public Response encouragement() {
        return Response.ok(this.arnold.encouragement().toJSON()).build();
    }
}
