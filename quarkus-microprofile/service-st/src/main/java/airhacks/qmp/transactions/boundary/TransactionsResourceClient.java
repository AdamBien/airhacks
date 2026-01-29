package airhacks.qmp.transactions.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("transactions")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TransactionsResourceClient {

    @POST
    @Path("deposit")
    Response deposit(JsonObject request);

    @POST
    @Path("withdraw")
    Response withdraw(JsonObject request);

    @POST
    @Path("transfer")
    Response transfer(JsonObject request);

    @GET
    @Path("account/{accountId}")
    Response transactionHistory(@PathParam("accountId") String accountId);

    @GET
    @Path("account/{accountId}")
    Response transactionHistory(
        @PathParam("accountId") String accountId,
        @QueryParam("from") String fromDate,
        @QueryParam("to") String toDate);
}
