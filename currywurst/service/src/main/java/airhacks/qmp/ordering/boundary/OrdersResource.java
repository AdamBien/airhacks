package airhacks.qmp.ordering.boundary;

import static airhacks.qmp.ordering.Requirement.Rn.R1_1;
import static airhacks.qmp.ordering.Requirement.Rn.R1_2;
import static airhacks.qmp.ordering.Requirement.Rn.R1_3;
import static airhacks.qmp.ordering.Requirement.Rn.R1_4;
import static airhacks.qmp.ordering.Requirement.Rn.R1_5;
import static airhacks.qmp.ordering.Requirement.Rn.R1_6;
import static airhacks.qmp.ordering.Requirement.Rn.R2_1;
import static airhacks.qmp.ordering.Requirement.Rn.R2_2;
import static airhacks.qmp.ordering.Requirement.Rn.R3_1;
import static airhacks.qmp.ordering.Requirement.Rn.R3_2;
import static airhacks.qmp.ordering.Requirement.Rn.R3_3;
import static airhacks.qmp.ordering.Requirement.Rn.R4_1;
import static airhacks.qmp.ordering.Requirement.Rn.R4_2;

import java.net.URI;
import java.util.List;

import airhacks.qmp.ordering.Requirement;
import airhacks.qmp.ordering.control.OrderBook;
import airhacks.qmp.ordering.entity.Order;
import airhacks.qmp.ordering.entity.OrderLine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrdersResource {

    @Inject
    OrderBook orderBook;

    /// `place-order` — accept order lines, validate and price them against `menu`, return the numbered order.
    @POST
    @Requirement({R1_1, R1_2, R1_3, R1_4, R1_5, R1_6})
    public Response placeOrder(JsonObject orderRequest) {
        var order = this.orderBook.place(lines(orderRequest));
        return Response.created(URI.create("orders/" + order.number()))
                .entity(order.toJSON())
                .build();
    }

    /// `get-order` — one of today's orders by its number.
    @GET
    @Path("{number}")
    @Requirement({R2_1, R2_2})
    public Response getOrder(@PathParam("number") int number) {
        var order = this.orderBook.find(number)
                .orElseThrow(() -> notFound(number));
        return Response.ok(order.toJSON()).build();
    }

    /// `cancel-order` — withdraw one of today's orders while it is still `placed`.
    @DELETE
    @Path("{number}")
    @Requirement({R3_1, R3_2, R3_3})
    public Response cancelOrder(@PathParam("number") int number) {
        try {
            var order = this.orderBook.cancel(number)
                    .orElseThrow(() -> notFound(number));
            return Response.ok(order.toJSON()).build();
        } catch (IllegalStateException e) {
            throw new ClientErrorException(e.getMessage(), Status.CONFLICT);
        }
    }

    /// `list-open-orders` — today's orders neither cancelled nor handed over, oldest first.
    @GET
    @Requirement({R4_1, R4_2})
    public Response listOpenOrders() {
        var orders = Json.createArrayBuilder();
        this.orderBook.open().stream()
                .map(Order::toJSON)
                .forEach(orders::add);
        return Response.ok(orders.build()).build();
    }

    static List<OrderLine> lines(JsonObject orderRequest) {
        var lines = orderRequest.getJsonArray("lines");
        if (lines == null) {
            return List.of();
        }
        return lines.getValuesAs(JsonObject.class).stream()
                .map(OrderLine::fromJSON)
                .toList();
    }

    static NotFoundException notFound(int number) {
        return new NotFoundException("no order %d today".formatted(number));
    }
}
