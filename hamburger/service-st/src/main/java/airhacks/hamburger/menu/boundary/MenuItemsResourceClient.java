package airhacks.hamburger.menu.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "base_uri")
public interface MenuItemsResourceClient {

    @GET
    List<MenuItem> listMenu();

    @GET
    @Path("{name}")
    Response findItem(@PathParam("name") String name);
}
