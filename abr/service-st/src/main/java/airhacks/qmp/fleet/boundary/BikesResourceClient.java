package airhacks.qmp.fleet.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

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

@Path("bikes")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BikesResourceClient {

    @POST
    Response registerBike(JsonObject registration);

    @GET
    @Path("available")
    Response listAvailableBikes(@QueryParam("type") String type);

    @GET
    @Path("count")
    Response countBikes(@QueryParam("type") String type);

    @POST
    @Path("{id}/withdraw")
    Response withdrawBike(@PathParam("id") String id);

    @POST
    @Path("{id}/restore")
    Response restoreBike(@PathParam("id") String id);

    @POST
    @Path("allocations")
    Response allocateBike(JsonObject request);

    @POST
    @Path("{id}/release")
    Response releaseBike(@PathParam("id") String id);
}
