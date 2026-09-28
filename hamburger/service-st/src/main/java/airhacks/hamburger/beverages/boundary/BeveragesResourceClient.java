package airhacks.hamburger.beverages.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("beverages")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "base_uri")
public interface BeveragesResourceClient {

    @GET
    List<Beverage> listBeverages();

    @GET
    @Path("{name}")
    Response findBeverage(@PathParam("name") String name);
}
