package airhacks.qmp.shoppingcarts.boundary;

import airhacks.qmp.shoppingcarts.control.ShoppingCartStore;
import airhacks.qmp.shoppingcarts.entity.ShoppingCart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("shoppingcarts")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShoppingCartsResource {

    @Inject
    ShoppingCartStore store;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(ShoppingCart::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @GET
    @Path("{customerId}")
    public Response findByCustomerId(@PathParam("customerId") String customerId) {
        var cart = this.store.findByCustomerId(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cart.toJSON()).build();
    }

    @PUT
    @Path("{customerId}")
    public Response save(@PathParam("customerId") String customerId, JsonObject json) {
        var cart = ShoppingCart.fromJSON(json);
        this.store.save(cart);
        return Response.ok(cart.toJSON()).build();
    }
}
