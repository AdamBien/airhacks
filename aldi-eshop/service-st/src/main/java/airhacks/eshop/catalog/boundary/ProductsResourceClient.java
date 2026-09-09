package airhacks.eshop.catalog.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("products")
@RegisterRestClient(configKey = "base_uri")
public interface ProductsResourceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonArray listProducts(@QueryParam("category") String category);

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject findProduct(@PathParam("id") String id);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listProductsRaw();
}
