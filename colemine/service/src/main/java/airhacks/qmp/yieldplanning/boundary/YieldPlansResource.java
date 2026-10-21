package airhacks.qmp.yieldplanning.boundary;

import static airhacks.qmp.yieldplanning.Requirement.Rn.*;
import static java.util.Map.entry;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import airhacks.qmp.yieldplanning.Requirement;
import airhacks.qmp.yieldplanning.control.YieldPlans;
import airhacks.qmp.yieldplanning.entity.YieldPlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("yield-plans")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class YieldPlansResource {

    @Inject
    YieldPlans plans;

    @POST
    @Requirement({ R1_1, R1_2, R1_3, R1_4, R6_2 })
    public Response planDay(JsonObject json) {
        var plan = this.plans.plan(YieldPlan.fromJSON(json));
        return Response.created(URI.create("yield-plans/" + plan.day()))
                .entity(plan.toJSON())
                .build();
    }

    @PUT
    @Path("{day}")
    @Requirement({ R2_1, R2_2, R2_3, R6_3 })
    public JsonObject revisePlan(@PathParam("day") String day, JsonObject json) {
        return this.plans.revise(dayOf(day), YieldPlan.tonnesFromJSON(json)).toJSON();
    }

    @DELETE
    @Path("{day}")
    @Requirement({ R3_1, R3_2, R6_4 })
    public void deletePlan(@PathParam("day") String day) {
        this.plans.delete(dayOf(day));
    }

    @GET
    @Path("{day}")
    @Requirement({ R4_1, R4_2 })
    public JsonObject getPlan(@PathParam("day") String day) {
        var parsed = dayOf(day);
        return this.plans.plan(parsed)
                .map(YieldPlan::toJSON)
                .orElseThrow(() -> new NotFoundException("no plan for " + parsed));
    }

    @GET
    @Requirement({ R5_1, R5_2, R5_3, R5_4 })
    public JsonArray listPlans(@QueryParam("from") String from, @QueryParam("to") String to) {
        var fromDay = YieldPlan.day(from);
        var toDay = YieldPlan.day(to);
        if (fromDay.isEmpty() || toDay.isEmpty())
            throw YieldPlan.invalidInput(entry("from", fromDay), entry("to", toDay));
        return this.plans.plans(fromDay.get(), toDay.get()).stream()
                .map(YieldPlan::toJSON)
                .collect(JsonCollectors.toJsonArray());
    }

    static LocalDate dayOf(String day) {
        return YieldPlan.day(day).orElseThrow(() -> YieldPlan.invalidInput(List.of(YieldPlan.DAY)));
    }
}
