package airhacks.qmp.ordering.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "base_uri")
public interface OrdersResourceClient {

    @POST
    Response placeOrder(JsonObject orderRequest);

    @GET
    @Path("{number}")
    Response getOrder(@PathParam("number") int number);

    @DELETE
    @Path("{number}")
    Response cancelOrder(@PathParam("number") int number);

    @GET
    JsonArray listOpenOrders();
}
