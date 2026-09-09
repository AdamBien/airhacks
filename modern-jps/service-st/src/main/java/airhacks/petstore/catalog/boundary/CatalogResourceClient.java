package airhacks.petstore.catalog.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("catalog")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "base_uri")
public interface CatalogResourceClient {

    @GET
    @Path("categories")
    Response categories();

    @GET
    @Path("categories/{categoryId}/products")
    Response products(@PathParam("categoryId") String categoryId);

    @GET
    @Path("products/{productId}/items")
    Response items(@PathParam("productId") String productId);

    @GET
    @Path("items/{itemId}")
    Response item(@PathParam("itemId") String itemId);

    @GET
    @Path("search")
    Response search(@QueryParam("keyword") String keyword);
}
