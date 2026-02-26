package airhacks.qmp.orders.boundary;

import airhacks.qmp.orders.control.OrderStore;
import airhacks.qmp.orders.entity.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("orders")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrdersResource {

    @Inject
    OrderStore store;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(Order::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var order = Order.fromJSON(json);
        this.store.add(order);
        return Response.status(Response.Status.CREATED)
                .entity(order.toJSON())
                .build();
    }
}
