package airhacks.qmp.workshops.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@QuarkusTest
class WorkshopsResourceIT {

    @Inject
    @RestClient
    WorkshopsResourceClient client;

    @Test
    void scheduleAndFetchWorkshop() {
        var airhacks = Json.createObjectBuilder()
                .add("title", "Spec-Driven Java Development at LLM Speed")
                .add("date", "2026-07-02")
                .add("capacity", 30)
                .build();

        String id;
        try (var response = this.client.schedule(airhacks)) {
            assertThat(response.getStatus()).isEqualTo(201);
            var created = response.readEntity(JsonObject.class);
            id = created.getString("id");
            assertThat(id).isNotBlank();
            assertThat(created.getInt("capacity")).isEqualTo(30);
        }

        try (var response = this.client.byId(id)) {
            assertThat(response.getStatus()).isEqualTo(200);
            var fetched = response.readEntity(JsonObject.class);
            assertThat(fetched.getString("title")).isEqualTo("Spec-Driven Java Development at LLM Speed");
        }
    }

    @Test
    void listWorkshops() {
        try (var response = this.client.all()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    void unknownWorkshopReturns404() {
        try (var response = this.client.byId("does-not-exist")) {
            assertThat(response.getStatus()).isEqualTo(404);
        }
    }
}
