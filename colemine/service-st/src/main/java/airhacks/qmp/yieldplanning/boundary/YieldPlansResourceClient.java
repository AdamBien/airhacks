package airhacks.qmp.yieldplanning.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("yield-plans")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface YieldPlansResourceClient {

    @POST
    Response planDay(JsonObject plan);

    @PUT
    @Path("{day}")
    Response revisePlan(@PathParam("day") String day, JsonObject tonnes);

    @DELETE
    @Path("{day}")
    Response deletePlan(@PathParam("day") String day);

    @GET
    @Path("{day}")
    Response getPlan(@PathParam("day") String day);

    @GET
    Response listPlans(@QueryParam("from") String from, @QueryParam("to") String to);
}
