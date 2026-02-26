package airhacks.qmp.customers.boundary;

import airhacks.qmp.customers.control.CustomerStore;
import airhacks.qmp.customers.entity.Customer;
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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("customers")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource {

    @Inject
    CustomerStore store;

    @Inject
    PaymentStore paymentStore;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(Customer::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var customer = Customer.fromJSON(json);
        this.store.add(customer);
        return Response.status(Response.Status.CREATED)
                .entity(customer.toJSON())
                .build();
    }

    @GET
    @Path("{customerId}/payments")
    public JsonArray payments(@PathParam("customerId") String customerId) {
        return this.paymentStore.findByCustomerId(customerId).stream()
                .map(Payment::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }
}
