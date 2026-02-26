package airhacks.qmp.shipments.boundary;

import airhacks.qmp.payments.control.PaymentStore;
import airhacks.qmp.shipments.control.ShipmentStore;
import airhacks.qmp.shipments.entity.Shipment;
import airhacks.qmp.shipments.entity.ShipmentWithoutPaymentException;
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

@Path("shipments")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentsResource {

    @Inject
    ShipmentStore store;

    @Inject
    PaymentStore paymentStore;

    @GET
    public JsonArray all() {
        return this.store.all().stream()
                .map(Shipment::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var shipment = Shipment.fromJSON(json);
        var payments = this.paymentStore.findByCustomerId(shipment.customerId());
        if (payments.isEmpty()) {
            throw new ShipmentWithoutPaymentException(shipment.customerId());
        }
        this.store.add(shipment);
        return Response.status(Response.Status.CREATED)
                .entity(shipment.toJSON())
                .build();
    }
}
