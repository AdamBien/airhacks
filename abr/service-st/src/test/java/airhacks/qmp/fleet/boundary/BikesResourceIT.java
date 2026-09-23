package airhacks.qmp.fleet.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/// Black-box walk through the fleet boundary against a running service.
/// Case labels carry the spec ids they exercise; the per-statement table lives in the unit tests.
@QuarkusTest
class BikesResourceIT {

    @Inject
    @RestClient
    BikesResourceClient rut;

    @Test
    @DisplayName("R1.1 R1.3 register, then reject the duplicate")
    void registerBike() {
        var id = uniqueId();
        var created = this.rut.registerBike(registration(id, "e-bike"));
        assertThat(created.getStatus()).isEqualTo(201);
        var bike = created.readEntity(JsonObject.class);
        assertThat(bike.getString("id")).isEqualTo(id);
        assertThat(bike.getString("status")).isEqualTo("available");

        var duplicate = this.rut.registerBike(registration(id, "e-bike"));
        assertThat(duplicate.getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("R1.2 reject an unknown bike type")
    void registerUnknownType() {
        var response = this.rut.registerBike(registration(uniqueId(), "unicycle"));
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("R2.1 R2.2 R3.1 R3.2 list and count reflect status and type")
    void listAndCount() {
        var bike = uniqueId();
        var ebike = uniqueId();
        this.rut.registerBike(registration(bike, "bike"));
        this.rut.registerBike(registration(ebike, "e-bike"));
        var countBefore = count(null);
        var ebikeCountBefore = count("e-bike");

        assertThat(availableIds(null)).contains(bike, ebike);
        assertThat(availableIds("e-bike")).contains(ebike).doesNotContain(bike);

        this.rut.withdrawBike(ebike);
        assertThat(availableIds(null)).contains(bike).doesNotContain(ebike);
        assertThat(count(null)).isEqualTo(countBefore - 1);
        assertThat(count("e-bike")).isEqualTo(ebikeCountBefore - 1);
    }

    @Test
    @DisplayName("R4.1 R4.3 R4.4 withdraw and restore, unknown bike is 404")
    void withdrawAndRestore() {
        var id = uniqueId();
        this.rut.registerBike(registration(id, "bike"));

        var withdrawn = this.rut.withdrawBike(id);
        assertThat(withdrawn.getStatus()).isEqualTo(200);
        assertThat(withdrawn.readEntity(JsonObject.class).getString("status")).isEqualTo("withdrawn");

        var restored = this.rut.restoreBike(id);
        assertThat(restored.getStatus()).isEqualTo(200);
        assertThat(restored.readEntity(JsonObject.class).getString("status")).isEqualTo("available");

        assertThat(this.rut.withdrawBike("ghost-" + id).getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("R5.1 R4.2 R5.3 R5.4 allocate, refuse withdrawal on the road, release once")
    void allocateAndRelease() {
        var id = uniqueId();
        this.rut.registerBike(registration(id, "e-bike"));

        var allocated = this.rut.allocateBike(Json.createObjectBuilder().add("type", "e-bike").build());
        assertThat(allocated.getStatus()).isEqualTo(200);
        var bike = allocated.readEntity(JsonObject.class);
        assertThat(bike.getString("status")).isEqualTo("allocated");
        var allocatedId = bike.getString("id");

        assertThat(this.rut.withdrawBike(allocatedId).getStatus()).isEqualTo(409);

        var released = this.rut.releaseBike(allocatedId);
        assertThat(released.getStatus()).isEqualTo(200);
        assertThat(released.readEntity(JsonObject.class).getString("status")).isEqualTo("available");

        assertThat(this.rut.releaseBike(allocatedId).getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("R5.2 refuse allocation when no bike of the type is available")
    void allocateWithoutAvailableBike() {
        while (!availableIds("bike").isEmpty()) {
            this.rut.allocateBike(Json.createObjectBuilder().add("type", "bike").build());
        }
        var response = this.rut.allocateBike(Json.createObjectBuilder().add("type", "bike").build());
        assertThat(response.getStatus()).isEqualTo(409);
    }

    static String uniqueId() {
        return "st-" + UUID.randomUUID();
    }

    static JsonObject registration(String id, String type) {
        return Json.createObjectBuilder().add("id", id).add("type", type).build();
    }

    java.util.List<String> availableIds(String type) {
        return this.rut.listAvailableBikes(type).readEntity(JsonArray.class).stream()
                .map(JsonObject.class::cast)
                .map(bike -> bike.getString("id"))
                .toList();
    }

    long count(String type) {
        return this.rut.countBikes(type).readEntity(JsonObject.class).getJsonNumber("count").longValue();
    }
}
