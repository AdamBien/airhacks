package airhacks.ebank.reports.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reports")
@Produces(MediaType.TEXT_PLAIN)
@RegisterRestClient(configKey = "ebank_uri")
public interface ReportsResourceClient {


    @GET
    @Path("accounts")
    public Response accounts();
}