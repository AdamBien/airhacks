package airhacks.qmp.reservation.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reservations")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ReservationsResourceClient {

    @POST
    Response reserveBike(JsonObject request);

    @POST
    @Path("{id}/cancel")
    Response cancelReservation(@PathParam("id") String id);

    @POST
    @Path("{id}/redeem")
    Response redeemReservation(@PathParam("id") String id);

    @GET
    @Path("{id}")
    Response findReservation(@PathParam("id") String id);
}
