package airhacks.ebank.reporting.boundary;

import java.util.stream.Collectors;

import airhacks.ebank.reporting.control.AccountQuery;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reports")
@Produces(MediaType.TEXT_PLAIN)
public class ReportsResource {

    @Inject
    AccountQuery accounts;

    /**
     * @return ibans as CSV
     */
    @GET
    @Path("accounts")
    public Response accounts() {
        var allAccounts = this.accounts.asIBANs();
        if (allAccounts.isEmpty())
            return Response
                    .noContent()
                    .build();
        var csv = allAccounts
                .stream()
                .collect(Collectors.joining(","));
        return Response
                .ok(csv)
                .build();
    }
}
