package airhacks.qmp.sessions.boundary;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "base_uri")
public interface SessionsResourceClient {

    @GET
    Response all();

    @GET
    @Path("{id}")
    Response byId(@PathParam("id") String id);

    @GET
    @Path("byWorkshop")
    Response byWorkshop(@QueryParam("workshopId") String workshopId);

    @POST
    Response add(JsonObject session);
}
