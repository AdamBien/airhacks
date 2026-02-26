package airhacks.qmp.products.boundary;

import airhacks.qmp.products.entity.CreateProductRequest;
import airhacks.qmp.products.entity.UpdateProductRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("products")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProductResourceClient {

    @POST
    Response create(@HeaderParam("Authorization") String authorization, CreateProductRequest request);

    @GET
    Response listActive();

    @GET
    @Path("{id}")
    Response getById(@PathParam("id") String id);

    @PUT
    @Path("{id}")
    Response update(@HeaderParam("Authorization") String authorization, @PathParam("id") String id,
            UpdateProductRequest request);

    @DELETE
    @Path("{id}")
    Response delete(@HeaderParam("Authorization") String authorization, @PathParam("id") String id);

    @GET
    @Path("seller")
    Response listBySeller(@HeaderParam("Authorization") String authorization);
}
