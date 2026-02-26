package airhacks.qmp.products.boundary;

import java.util.UUID;

import airhacks.qmp.products.control.ProductStore;
import airhacks.qmp.products.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("products")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductsResource {

    @Inject
    ProductStore store;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(Product::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var parsed = Product.fromJSON(json);
        var productId = UUID.randomUUID().toString();
        var product = new Product(productId, parsed.name(), parsed.description(), parsed.price(), parsed.color());
        this.store.add(product);
        return Response.status(Response.Status.CREATED)
                .entity(product.toJSON())
                .build();
    }

    @GET
    @Path("{productId}")
    public Response find(@PathParam("productId") String productId) {
        return this.store.findById(productId)
                .map(product -> Response.ok(product.toJSON()).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
