package airhacks.qmp.accounts.boundary;

import java.net.URI;

import airhacks.qmp.accounts.control.AccountProcessor;
import airhacks.qmp.accounts.entity.AccountHolder;
import airhacks.qmp.accounts.entity.Currency;
import airhacks.qmp.accounts.entity.Region;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
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

/**
 * JAX-RS resource for account lifecycle operations.
 * Delegates business logic to AccountProcessor.
 */
@Path("accounts")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountsResource {

    @Inject
    AccountProcessor accountProcessor;

    @POST
    public Response createAccount(JsonObject request) {
        var validationError = validateCreateRequest(request);
        if (validationError != null) {
            return validationError;
        }

        var holderJson = request.getJsonObject("holder");
        var holder = AccountHolder.fromJson(holderJson);
        
        Currency currency;
        try {
            currency = Currency.valueOf(request.getString("currency"));
        } catch (IllegalArgumentException e) {
            return badRequest("Unsupported currency: " + request.getString("currency"));
        }
        
        var region = Region.valueOf(request.getString("region"));
        var account = this.accountProcessor.create(holder, currency, region);
        
        return Response.created(URI.create("/accounts/" + account.id()))
            .entity(account.toJson())
            .build();
    }

    @GET
    @Path("{id}")
    public Response account(@PathParam("id") String accountId) {
        return this.accountProcessor.find(accountId)
            .map(account -> Response.ok(account.toJson()).build())
            .orElseGet(() -> notFound("Account not found: " + accountId));
    }

    @GET
    @Path("holder/{holderId}")
    public Response accountsByHolder(@PathParam("holderId") String holderId) {
        var accounts = this.accountProcessor.findByHolder(holderId);
        var arrayBuilder = Json.createArrayBuilder();
        accounts.stream()
            .map(account -> account.toJson())
            .forEach(arrayBuilder::add);
        
        return Response.ok(arrayBuilder.build()).build();
    }

    @DELETE
    @Path("{id}")
    public Response closeAccount(@PathParam("id") String accountId) {
        var accountOpt = this.accountProcessor.find(accountId);
        if (accountOpt.isEmpty()) {
            return notFound("Account not found: " + accountId);
        }
        
        try {
            var closedAccount = this.accountProcessor.close(accountId);
            return Response.ok(closedAccount.toJson()).build();
        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    Response validateCreateRequest(JsonObject request) {
        if (!request.containsKey("holder")) {
            return badRequest("Holder information is required");
        }
        
        var holderJson = request.getJsonObject("holder");
        if (!holderJson.containsKey("name") || holderJson.getString("name").isBlank()) {
            return badRequest("Holder name is required");
        }
        
        if (!request.containsKey("currency")) {
            return badRequest("Currency is required");
        }
        
        if (!request.containsKey("region")) {
            return badRequest("Region is required");
        }
        
        try {
            Region.valueOf(request.getString("region"));
        } catch (IllegalArgumentException e) {
            return badRequest("Invalid region: " + request.getString("region"));
        }
        
        return null;
    }

    Response badRequest(String message) {
        var error = Json.createObjectBuilder()
            .add("error", message)
            .build();
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(error)
            .build();
    }

    Response notFound(String message) {
        var error = Json.createObjectBuilder()
            .add("error", message)
            .build();
        return Response.status(Response.Status.NOT_FOUND)
            .entity(error)
            .build();
    }
}
