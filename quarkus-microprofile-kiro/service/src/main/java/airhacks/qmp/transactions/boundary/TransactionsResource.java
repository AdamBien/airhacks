package airhacks.qmp.transactions.boundary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import airhacks.qmp.accounts.entity.Currency;
import airhacks.qmp.transactions.control.TransactionProcessor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
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

/**
 * JAX-RS resource for financial transaction operations.
 * Delegates business logic to TransactionProcessor.
 */
@Path("transactions")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsResource {

    @Inject
    TransactionProcessor transactionProcessor;

    @POST
    @Path("deposit")
    public Response deposit(JsonObject request) {
        var validationError = validateDepositRequest(request);
        if (validationError != null) {
            return validationError;
        }

        var accountId = request.getString("accountId");
        var amount = request.getJsonNumber("amount").bigDecimalValue();
        
        Currency currency;
        try {
            currency = Currency.valueOf(request.getString("currency"));
        } catch (IllegalArgumentException e) {
            return badRequest("Unsupported currency: " + request.getString("currency"));
        }

        try {
            var transaction = this.transactionProcessor.deposit(accountId, amount, currency);
            return Response.ok(transaction.toJson()).build();
        } catch (IllegalArgumentException e) {
            return handleException(e);
        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    @POST
    @Path("withdraw")
    public Response withdraw(JsonObject request) {
        var validationError = validateWithdrawRequest(request);
        if (validationError != null) {
            return validationError;
        }

        var accountId = request.getString("accountId");
        var amount = request.getJsonNumber("amount").bigDecimalValue();

        try {
            var transaction = this.transactionProcessor.withdraw(accountId, amount);
            return Response.ok(transaction.toJson()).build();
        } catch (IllegalArgumentException e) {
            return handleException(e);
        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    @POST
    @Path("transfer")
    public Response transfer(JsonObject request) {
        var validationError = validateTransferRequest(request);
        if (validationError != null) {
            return validationError;
        }

        var sourceAccountId = request.getString("sourceAccountId");
        var destinationAccountId = request.getString("destinationAccountId");
        var amount = request.getJsonNumber("amount").bigDecimalValue();

        try {
            var result = this.transactionProcessor.transfer(sourceAccountId, destinationAccountId, amount);
            var responseJson = Json.createObjectBuilder()
                .add("sourceTransaction", result.sourceTransaction().toJson())
                .add("destinationTransaction", result.destinationTransaction().toJson())
                .build();
            return Response.ok(responseJson).build();
        } catch (IllegalArgumentException e) {
            return handleException(e);
        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    @GET
    @Path("account/{accountId}")
    public Response transactionHistory(
            @PathParam("accountId") String accountId,
            @QueryParam("from") String fromDate,
            @QueryParam("to") String toDate) {

        LocalDate from = null;
        LocalDate to = null;

        try {
            if (fromDate != null && !fromDate.isBlank()) {
                from = LocalDate.parse(fromDate);
            }
            if (toDate != null && !toDate.isBlank()) {
                to = LocalDate.parse(toDate);
            }
        } catch (DateTimeParseException e) {
            return badRequest("Invalid date format. Use ISO format: YYYY-MM-DD");
        }

        try {
            var transactions = this.transactionProcessor.history(accountId, from, to);
            var arrayBuilder = Json.createArrayBuilder();
            transactions.stream()
                .map(t -> t.toJson())
                .forEach(arrayBuilder::add);
            return Response.ok(arrayBuilder.build()).build();
        } catch (IllegalArgumentException e) {
            return handleException(e);
        }
    }

    Response validateDepositRequest(JsonObject request) {
        if (!request.containsKey("accountId") || request.getString("accountId").isBlank()) {
            return badRequest("Account ID is required");
        }
        if (!request.containsKey("amount")) {
            return badRequest("Amount is required");
        }
        var amount = request.getJsonNumber("amount").bigDecimalValue();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return badRequest("Amount must be positive");
        }
        if (!request.containsKey("currency") || request.getString("currency").isBlank()) {
            return badRequest("Currency is required");
        }
        return null;
    }

    Response validateWithdrawRequest(JsonObject request) {
        if (!request.containsKey("accountId") || request.getString("accountId").isBlank()) {
            return badRequest("Account ID is required");
        }
        if (!request.containsKey("amount")) {
            return badRequest("Amount is required");
        }
        var amount = request.getJsonNumber("amount").bigDecimalValue();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return badRequest("Amount must be positive");
        }
        return null;
    }

    Response validateTransferRequest(JsonObject request) {
        if (!request.containsKey("sourceAccountId") || request.getString("sourceAccountId").isBlank()) {
            return badRequest("Source account ID is required");
        }
        if (!request.containsKey("destinationAccountId") || request.getString("destinationAccountId").isBlank()) {
            return badRequest("Destination account ID is required");
        }
        if (!request.containsKey("amount")) {
            return badRequest("Amount is required");
        }
        var amount = request.getJsonNumber("amount").bigDecimalValue();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return badRequest("Amount must be positive");
        }
        return null;
    }

    Response handleException(IllegalArgumentException e) {
        var message = e.getMessage();
        if (message != null && message.contains("Account not found")) {
            return notFound(message);
        }
        return badRequest(message);
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
