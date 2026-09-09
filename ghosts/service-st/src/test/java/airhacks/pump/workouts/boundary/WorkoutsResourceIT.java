package airhacks.pump.workouts.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@QuarkusTest
class WorkoutsResourceIT {

    @Inject
    @RestClient
    WorkoutsResourceClient rut;

    @Test
    void loggedWorkoutIsMotivated() {
        try (var response = this.rut.add(squats())) {
            assertThat(response.getStatus()).isEqualTo(201);
            var logged = response.readEntity(JsonObject.class);
            assertThat(logged.getJsonObject("workout").getJsonNumber("volumeKg").doubleValue())
                    .isEqualTo(5 * 10 * 120.0);
            assertThat(logged.getString("motivation")).isNotBlank();
        }
    }

    @Test
    void sessionAggregatesVolume() {
        try (var _ = this.rut.add(squats())) {
            var session = this.rut.session();
            assertThat(session.getJsonArray("workouts")).isNotEmpty();
            assertThat(session.getJsonNumber("totalVolumeKg").doubleValue())
                    .isGreaterThanOrEqualTo(5 * 10 * 120.0);
            assertThat(session.getString("verdict")).isNotBlank();
        }
    }

    @Test
    void girlyPayloadIsRejected() {
        try (var response = this.rut.add(Json.createObjectBuilder().build())) {
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    static JsonObject squats() {
        return Json.createObjectBuilder()
                .add("exercise", "SQUAT")
                .add("sets", 5)
                .add("reps", 10)
                .add("weightKg", 120.0)
                .build();
    }
}
