package airhacks.qmp.fleet.boundary;

import static airhacks.qmp.fleet.Requirement.Rn.R1_1;
import static airhacks.qmp.fleet.Requirement.Rn.R1_2;
import static airhacks.qmp.fleet.Requirement.Rn.R1_3;
import static airhacks.qmp.fleet.Requirement.Rn.R2_1;
import static airhacks.qmp.fleet.Requirement.Rn.R2_2;
import static airhacks.qmp.fleet.Requirement.Rn.R3_1;
import static airhacks.qmp.fleet.Requirement.Rn.R3_2;
import static airhacks.qmp.fleet.Requirement.Rn.R4_1;
import static airhacks.qmp.fleet.Requirement.Rn.R4_2;
import static airhacks.qmp.fleet.Requirement.Rn.R4_3;
import static airhacks.qmp.fleet.Requirement.Rn.R4_4;
import static airhacks.qmp.fleet.Requirement.Rn.R5_1;
import static airhacks.qmp.fleet.Requirement.Rn.R5_2;
import static airhacks.qmp.fleet.Requirement.Rn.R5_3;
import static airhacks.qmp.fleet.Requirement.Rn.R5_4;

import airhacks.qmp.fleet.Requirement;
import airhacks.qmp.fleet.control.Bikes;
import airhacks.qmp.fleet.entity.Bike;
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

@Path("bikes")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BikesResource {

    @Inject
    Bikes bikes;

    @POST
    @Requirement({R1_1, R1_2, R1_3})
    public Response registerBike(JsonObject registration) {
        var bike = this.bikes.registerBike(
                registration.getString("id", null),
                registration.getString("type", null));
        return Response.status(Response.Status.CREATED).entity(bike.toJSON()).build();
    }

    @GET
    @Path("available")
    @Requirement({R2_1, R2_2})
    public Response listAvailableBikes(@QueryParam("type") String type) {
        var available = this.bikes.listAvailableBikes(Bikes.optionalTypeOf(type));
        var json = Json.createArrayBuilder();
        available.stream().map(Bike::toJSON).forEach(json::add);
        return Response.ok(json.build()).build();
    }

    @GET
    @Path("count")
    @Requirement({R3_1, R3_2})
    public Response countBikes(@QueryParam("type") String type) {
        var count = this.bikes.countBikes(Bikes.optionalTypeOf(type));
        return Response.ok(Json.createObjectBuilder().add("count", count).build()).build();
    }

    @POST
    @Path("{id}/withdraw")
    @Requirement({R4_1, R4_2, R4_4})
    public Response withdrawBike(@PathParam("id") String id) {
        return Response.ok(this.bikes.withdrawBike(id).toJSON()).build();
    }

    @POST
    @Path("{id}/restore")
    @Requirement({R4_3, R4_4})
    public Response restoreBike(@PathParam("id") String id) {
        return Response.ok(this.bikes.restoreBike(id).toJSON()).build();
    }

    @POST
    @Path("allocations")
    @Requirement({R5_1, R5_2})
    public Response allocateBike(JsonObject request) {
        var type = Bikes.typeOf(request.getString("type", null));
        return Response.ok(this.bikes.allocateBike(type).toJSON()).build();
    }

    @POST
    @Path("{id}/release")
    @Requirement({R5_3, R5_4})
    public Response releaseBike(@PathParam("id") String id) {
        return Response.ok(this.bikes.releaseBike(id).toJSON()).build();
    }
}
