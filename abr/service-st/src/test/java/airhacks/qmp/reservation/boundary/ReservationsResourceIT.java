package airhacks.qmp.reservation.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airhacks.qmp.fleet.boundary.BikesResourceClient;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/// Black-box walk through the reservation boundary against a running service.
/// Case labels carry the spec ids they exercise; the per-statement table lives in the unit tests.
@QuarkusTest
class ReservationsResourceIT {

    static final ZoneId AMSTERDAM = ZoneId.of("Europe/Amsterdam");

    @Inject
    @RestClient
    ReservationsResourceClient rut;

    @Inject
    @RestClient
    BikesResourceClient bikes;

    @Test
    @DisplayName("R1.1 R5.1 R3.1 reserve for today, find it, redeem it at pickup")
    void reserveFindRedeem() {
        ensureBike("e-bike");
        var created = this.rut.reserveBike(reservation("e-bike", today()));
        assertThat(created.getStatus()).isEqualTo(201);
        var id = created.readEntity(JsonObject.class).getString("id");

        var found = this.rut.findReservation(id);
        assertThat(found.getStatus()).isEqualTo(200);
        assertThat(found.readEntity(JsonObject.class).getString("status")).isEqualTo("open");

        var redeemed = this.rut.redeemReservation(id);
        assertThat(redeemed.getStatus()).isEqualTo(200);
        assertThat(redeemed.readEntity(JsonObject.class).getString("status")).isEqualTo("redeemed");
    }

    @Test
    @DisplayName("R1.2 R1.3 R1.4 reject past day, unknown type, missing contact")
    void rejectInvalidReservations() {
        assertThat(this.rut.reserveBike(reservation("bike", today().minusDays(1))).getStatus()).isEqualTo(400);
        assertThat(this.rut.reserveBike(reservation("unicycle", today())).getStatus()).isEqualTo(400);
        var noContact = Json.createObjectBuilder()
                .add("type", "bike")
                .add("pickupDay", today().toString())
                .build();
        assertThat(this.rut.reserveBike(noContact).getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("R1.5 refuse the reservation that would overbook a day")
    void refuseOverbooking() {
        ensureBike("bike");
        var farDay = today().plusDays(ThreadLocalRandom.current().nextInt(1000, 5000));
        var capacity = this.bikes.countBikes("bike").readEntity(JsonObject.class).getJsonNumber("count").longValue();
        for (var i = 0; i < capacity; i++) {
            assertThat(this.rut.reserveBike(reservation("bike", farDay)).getStatus()).isEqualTo(201);
        }
        assertThat(this.rut.reserveBike(reservation("bike", farDay)).getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("R2.1 R2.2 R3.3 cancel once, then refuse cancel and redeem")
    void cancelReservation() {
        ensureBike("e-bike");
        var id = this.rut.reserveBike(reservation("e-bike", today().plusDays(1)))
                .readEntity(JsonObject.class).getString("id");

        var cancelled = this.rut.cancelReservation(id);
        assertThat(cancelled.getStatus()).isEqualTo(200);
        assertThat(cancelled.readEntity(JsonObject.class).getString("status")).isEqualTo("cancelled");

        assertThat(this.rut.cancelReservation(id).getStatus()).isEqualTo(409);
        assertThat(this.rut.redeemReservation(id).getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("R3.2 refuse redemption before the pickup day")
    void redeemTooEarly() {
        ensureBike("e-bike");
        var id = this.rut.reserveBike(reservation("e-bike", today().plusDays(1)))
                .readEntity(JsonObject.class).getString("id");
        assertThat(this.rut.redeemReservation(id).getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("R5.2 unknown reservation is 404")
    void findUnknown() {
        assertThat(this.rut.findReservation("ghost-" + UUID.randomUUID()).getStatus()).isEqualTo(404);
    }

    static LocalDate today() {
        return LocalDate.now(AMSTERDAM);
    }

    static JsonObject reservation(String type, LocalDate pickupDay) {
        return Json.createObjectBuilder()
                .add("customer", Json.createObjectBuilder().add("name", "Duke").add("email", "duke@java.net"))
                .add("type", type)
                .add("pickupDay", pickupDay.toString())
                .build();
    }

    /// reservations need capacity: make sure at least one in-service bike of the type exists
    void ensureBike(String type) {
        this.bikes.registerBike(Json.createObjectBuilder()
                .add("id", "st-reservation-" + UUID.randomUUID())
                .add("type", type)
                .build());
    }
}
