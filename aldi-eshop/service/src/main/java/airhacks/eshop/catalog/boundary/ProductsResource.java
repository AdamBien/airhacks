package airhacks.eshop.catalog.boundary;

import static airhacks.eshop.catalog.Requirement.Rn.R1_1;
import static airhacks.eshop.catalog.Requirement.Rn.R1_2;
import static airhacks.eshop.catalog.Requirement.Rn.R1_3;
import static airhacks.eshop.catalog.Requirement.Rn.R1_4;
import static airhacks.eshop.catalog.Requirement.Rn.R2_1;
import static airhacks.eshop.catalog.Requirement.Rn.R2_2;

import java.util.Optional;

import airhacks.eshop.catalog.Requirement;
import airhacks.eshop.catalog.control.ProductStore;
import airhacks.eshop.catalog.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class ProductsResource {

    @Inject
    ProductStore store;

    @GET
    @Requirement({R1_1, R1_2, R1_3, R1_4})
    public Response listProducts(@QueryParam("category") String category) {
        var products = this.store.list(Optional.ofNullable(category))
                .stream()
                .map(Product::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(products).cacheControl(catalogCacheControl()).build();
    }

    @GET
    @Path("{id}")
    @Requirement({R2_1, R2_2})
    public Response findProduct(@PathParam("id") String id) {
        var product = this.store.find(id)
                .orElseThrow(() -> new NotFoundException("no product with id: " + id));
        return Response.ok(product.toJSON()).cacheControl(catalogCacheControl()).build();
    }

    static CacheControl catalogCacheControl() {
        var cacheControl = new CacheControl();
        cacheControl.setMaxAge(60);
        return cacheControl;
    }
}
