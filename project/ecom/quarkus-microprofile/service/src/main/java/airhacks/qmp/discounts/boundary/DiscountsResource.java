package airhacks.qmp.discounts.boundary;

import java.util.UUID;

import airhacks.qmp.discounts.control.DiscountStore;
import airhacks.qmp.discounts.entity.Discount;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Path("discounts")
@ApplicationScoped
@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DiscountsResource {

    @Inject
    DiscountStore store;

    @GET
    public JsonArray all(@QueryParam("active") boolean activeOnly) {
        var discounts = activeOnly ? this.store.activeDiscounts() : this.store.all();
        return discounts.stream()
                .map(Discount::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject json) {
        var parsed = Discount.fromJSON(json);
        var discountId = UUID.randomUUID().toString();
        var discount = new Discount(discountId, parsed.code(), parsed.description(), 
                parsed.percentage(), parsed.startDate(), parsed.endDate());
        this.store.add(discount);
        return Response.status(Response.Status.CREATED)
                .entity(discount.toJSON())
                .build();
    }

    @GET
    @Path("{discountId}")
    public Response find(@PathParam("discountId") String discountId) {
        return this.store.findById(discountId)
                .map(discount -> Response.ok(discount.toJSON()).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("code/{code}")
    public Response findByCode(@PathParam("code") String code) {
        return this.store.findByCode(code)
                .map(discount -> Response.ok(discount.toJSON()).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
