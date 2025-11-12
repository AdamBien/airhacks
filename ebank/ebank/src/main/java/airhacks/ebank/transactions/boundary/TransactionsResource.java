package airhacks.ebank.transactions.boundary;

import airhacks.ebank.Boundary;
import airhacks.ebank.accounting.boundary.TransactionCarrier;
import airhacks.ebank.accounting.control.Responses;
import airhacks.ebank.logging.control.EBLog;
import airhacks.ebank.transactions.control.TransactionProcessor;
import airhacks.ebank.transactions.entity.Transaction;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Boundary
@Path("transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionsResource {

    @Inject
    TransactionProcessor processor;

    @Inject
    EBLog log;


    @POST
    @Path("/{iban}/")
    public Response processTransaction(@PathParam("iban") String iban,TransactionCarrier serializedTransaction) {
        this.log.info("processTransaction " + iban);
        var transaction = Transaction.from(serializedTransaction);
        return processor
                .processTransaction(iban, transaction)
                .map(Responses::ok)
                .orElseGet(Responses::noContent);
    }
}
