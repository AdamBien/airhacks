package airhacks.qmp.health.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/q/health")
@RegisterRestClient(configKey = "base_uri")
public interface HealthClient {

    @GET
    @Path("/live")
    Response liveness();

    @GET
    @Path("/ready")
    Response readiness();
}
