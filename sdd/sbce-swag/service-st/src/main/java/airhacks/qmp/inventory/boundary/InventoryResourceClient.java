package airhacks.qmp.inventory.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("inventory")
@RegisterRestClient(configKey = "base_uri")
public interface InventoryResourceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response setStock(StockRequest request);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response availability();
}
