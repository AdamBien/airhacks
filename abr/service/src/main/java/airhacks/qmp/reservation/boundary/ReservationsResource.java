package airhacks.qmp.reservation.boundary;

import static airhacks.qmp.reservation.Requirement.Rn.R1_1;
import static airhacks.qmp.reservation.Requirement.Rn.R1_2;
import static airhacks.qmp.reservation.Requirement.Rn.R1_3;
import static airhacks.qmp.reservation.Requirement.Rn.R1_4;
import static airhacks.qmp.reservation.Requirement.Rn.R1_5;
import static airhacks.qmp.reservation.Requirement.Rn.R2_1;
import static airhacks.qmp.reservation.Requirement.Rn.R2_2;
import static airhacks.qmp.reservation.Requirement.Rn.R3_1;
import static airhacks.qmp.reservation.Requirement.Rn.R3_2;
import static airhacks.qmp.reservation.Requirement.Rn.R3_3;
import static airhacks.qmp.reservation.Requirement.Rn.R4_1;
import static airhacks.qmp.reservation.Requirement.Rn.R5_1;
import static airhacks.qmp.reservation.Requirement.Rn.R5_2;

import airhacks.qmp.reservation.Requirement;
import airhacks.qmp.reservation.control.Reservations;
import airhacks.qmp.reservation.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reservations")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReservationsResource {

    @Inject
    Reservations reservations;

    @POST
    @Requirement({R1_1, R1_2, R1_3, R1_4, R1_5})
    public Response reserveBike(JsonObject request) {
        var reservation = this.reservations.reserveBike(
                Customer.fromJSON(request.getJsonObject("customer")),
                request.getString("type", null),
                Reservations.pickupDayOf(request.getString("pickupDay", null)));
        return Response.status(Response.Status.CREATED)
                .entity(reservation.toJSON(this.reservations.today()))
                .build();
    }

    @POST
    @Path("{id}/cancel")
    @Requirement({R2_1, R2_2})
    public Response cancelReservation(@PathParam("id") String id) {
        var reservation = this.reservations.cancelReservation(id);
        return Response.ok(reservation.toJSON(this.reservations.today())).build();
    }

    @POST
    @Path("{id}/redeem")
    @Requirement({R3_1, R3_2, R3_3})
    public Response redeemReservation(@PathParam("id") String id) {
        var reservation = this.reservations.redeemReservation(id);
        return Response.ok(reservation.toJSON(this.reservations.today())).build();
    }

    @GET
    @Path("{id}")
    @Requirement({R4_1, R5_1, R5_2})
    public Response findReservation(@PathParam("id") String id) {
        var reservation = this.reservations.findReservation(id);
        return Response.ok(reservation.toJSON(this.reservations.today())).build();
    }
}
