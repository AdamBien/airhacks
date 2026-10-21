package airhacks.qmp.yieldplanning.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/// Black box against the running service: plans live in memory there,
/// so every test picks its own far-future day to stay independent of earlier runs.
@QuarkusTest
class YieldPlansResourceIT {

    @Inject
    @RestClient
    YieldPlansResourceClient client;

    static LocalDate unusedDay() {
        return LocalDate.of(2100, 1, 1).plusDays(ThreadLocalRandom.current().nextInt(300_000));
    }

    static JsonObject plan(LocalDate day, long tonnes, int stoneSize) {
        return Json.createObjectBuilder()
                .add("day", day.toString())
                .add("tonnes", tonnes)
                .add("stoneSize", stoneSize)
                .build();
    }

    @Test
    @DisplayName("R1.1, R1.3, R4.1 — plan a day once, read it back")
    void planDay() {
        var day = unusedDay();
        assertThat(this.client.planDay(plan(day, 12_500, 50)).getStatus()).isEqualTo(201);
        assertThat(this.client.planDay(plan(day, 9_000, 80)).getStatus()).isEqualTo(409);

        var fetched = this.client.getPlan(day.toString());
        assertThat(fetched.getStatus()).isEqualTo(200);
        assertThat(fetched.readEntity(JsonObject.class)).isEqualTo(plan(day, 12_500, 50));
    }

    @Test
    @DisplayName("R1.4 — reject an invalid plan, naming every invalid part")
    void rejectInvalidPlan() {
        var invalid = Json.createObjectBuilder().add("tonnes", -1).build();
        var response = this.client.planDay(invalid);
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.readEntity(JsonObject.class))
                .isEqualTo(Json.createObjectBuilder()
                        .add("invalid", Json.createArrayBuilder().add("day").add("tonnes").add("stoneSize"))
                        .build());
    }

    @Test
    @DisplayName("R2.2, R2.4, R2.5, R2.6 — revise a planned day's stone size, keep its tonnage")
    void revisePlan() {
        var day = unusedDay();
        var stoneSize = Json.createObjectBuilder().add("stoneSize", 80).build();
        assertThat(this.client.revisePlan(day.toString(), stoneSize).getStatus()).isEqualTo(404);
        this.client.planDay(plan(day, 12_500, 50));

        var revised = this.client.revisePlan(day.toString(), stoneSize);
        assertThat(revised.getStatus()).isEqualTo(200);
        assertThat(revised.readEntity(JsonObject.class)).isEqualTo(plan(day, 12_500, 80));
        assertThat(this.client.revisePlan(day.toString(), JsonObject.EMPTY_JSON_OBJECT).getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("R3.1, R3.2, R4.2 — delete a plan, then it is gone")
    void deletePlan() {
        var day = unusedDay();
        this.client.planDay(plan(day, 12_500, 50));
        assertThat(this.client.deletePlan(day.toString()).getStatus()).isEqualTo(204);
        assertThat(this.client.getPlan(day.toString()).getStatus()).isEqualTo(404);
        assertThat(this.client.deletePlan(day.toString()).getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("R5.1, R5.2, R5.3 — list plans in an inclusive range, ordered by day")
    void listPlans() {
        var first = unusedDay();
        var last = first.plusDays(4);
        this.client.planDay(plan(last, 9_000, 80));
        this.client.planDay(plan(first, 12_500, 50));

        var listed = this.client.listPlans(first.toString(), last.toString());
        assertThat(listed.getStatus()).isEqualTo(200);
        assertThat(listed.readEntity(JsonArray.class))
                .startsWith(plan(first, 12_500, 50))
                .endsWith(plan(last, 9_000, 80));
        assertThat(this.client.listPlans(last.toString(), first.toString()).getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("R6.2 — reject planning a day that has started")
    void rejectStartedDay() {
        var today = LocalDate.now(ZoneId.of("Europe/Warsaw"));
        assertThat(this.client.planDay(plan(today, 12_500, 50)).getStatus()).isEqualTo(409);
    }
}
