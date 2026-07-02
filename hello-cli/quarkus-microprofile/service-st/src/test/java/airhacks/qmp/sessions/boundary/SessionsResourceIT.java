package airhacks.qmp.sessions.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@QuarkusTest
class SessionsResourceIT {

    @Inject
    @RestClient
    SessionsResourceClient client;

    @Test
    void addAndFetchSession() {
        var session = Json.createObjectBuilder()
                .add("title", "Spec-Driven Java Development")
                .add("speaker", "Adam Bien")
                .add("duration", 60)
                .add("workshopId", "workshop-123")
                .build();

        String id;
        try (var response = this.client.add(session)) {
            assertThat(response.getStatus()).isEqualTo(201);
            var created = response.readEntity(JsonObject.class);
            id = created.getString("id");
            assertThat(id).isNotBlank();
            assertThat(created.getString("speaker")).isEqualTo("Adam Bien");
        }

        try (var response = this.client.byId(id)) {
            assertThat(response.getStatus()).isEqualTo(200);
            var fetched = response.readEntity(JsonObject.class);
            assertThat(fetched.getString("title")).isEqualTo("Spec-Driven Java Development");
        }
    }

    @Test
    void listSessions() {
        try (var response = this.client.all()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    void filterSessionsByWorkshop() {
        try (var response = this.client.byWorkshop("workshop-123")) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    void unknownSessionReturns404() {
        try (var response = this.client.byId("does-not-exist")) {
            assertThat(response.getStatus()).isEqualTo(404);
        }
    }
}
