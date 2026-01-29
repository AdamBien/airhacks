package airhacks.qmp.accounts.boundary;

import airhacks.qmp.accounts.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("accounts")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountsResource {

    @GET
    public JsonObject accounts() {
        var emptyArray = Json.createArrayBuilder().build();
        return Json.createObjectBuilder()
                .add("accounts", emptyArray)
                .build();
    }

    @POST
    public Response create(JsonObject input) {
        var account = Account.fromJSON(input);
        return Response.status(Response.Status.CREATED)
                .entity(account.toJSON())
                .build();
    }
}
