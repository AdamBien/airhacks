package airhacks.qmp.inventory.boundary;

import airhacks.qmp.inventory.control.Inventory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("inventory")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @Inject
    Inventory inventory;

    @POST
    public Response setStock(StockRequest request) {
        this.inventory.set(request.item(), request.size(), request.quantity());
        return Response.noContent().build();
    }

    @GET
    public Response availability() {
        return Response.ok(this.inventory.availability()).build();
    }
}
