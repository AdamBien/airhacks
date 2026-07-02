package airhacks.qmp.swag.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("claims")
@RegisterRestClient(configKey = "base_uri")
public interface ClaimsResourceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response claim(Claim claim);
}
