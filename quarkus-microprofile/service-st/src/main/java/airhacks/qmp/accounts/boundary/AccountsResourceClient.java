package airhacks.qmp.accounts.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("accounts")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AccountsResourceClient {

    @POST
    Response createAccount(JsonObject request);

    @GET
    @Path("{id}")
    Response account(@PathParam("id") String accountId);

    @GET
    @Path("holder/{holderId}")
    Response accountsByHolder(@PathParam("holderId") String holderId);

    @DELETE
    @Path("{id}")
    Response closeAccount(@PathParam("id") String accountId);
}
