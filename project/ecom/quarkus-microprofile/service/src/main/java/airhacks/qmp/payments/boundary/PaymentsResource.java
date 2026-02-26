package airhacks.qmp.payments.boundary;

import airhacks.qmp.payments.control.PaymentStore;
import airhacks.qmp.payments.entity.Payment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("payments")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentsResource {

    @Inject
    PaymentStore store;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(Payment::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var payment = Payment.fromJSON(json);
        this.store.add(payment);
        return Response.status(Response.Status.CREATED)
                .entity(payment.toJSON())
                .build();
    }
}
