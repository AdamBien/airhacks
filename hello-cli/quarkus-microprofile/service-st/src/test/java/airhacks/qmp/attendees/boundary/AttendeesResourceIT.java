package airhacks.qmp.attendees.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import airhacks.qmp.workshops.boundary.WorkshopsResourceClient;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@QuarkusTest
class AttendeesResourceIT {

    @Inject
    @RestClient
    AttendeesResourceClient client;

    @Inject
    @RestClient
    WorkshopsResourceClient workshops;

    String scheduleWorkshop(int capacity) {
        var workshop = Json.createObjectBuilder()
                .add("title", "airhacks.live")
                .add("date", "2026-07-02")
                .add("capacity", capacity)
                .build();
        try (var response = this.workshops.schedule(workshop)) {
            return response.readEntity(JsonObject.class).getString("id");
        }
    }

    JsonObject attendee(String workshopId) {
        return Json.createObjectBuilder()
                .add("name", "Duke")
                .add("email", "duke@airhacks.live")
                .add("workshopId", workshopId)
                .build();
    }

    @Test
    void registerAndFetchAttendee() {
        var workshopId = this.scheduleWorkshop(30);

        String id;
        try (var response = this.client.register(this.attendee(workshopId))) {
            assertThat(response.getStatus()).isEqualTo(201);
            var created = response.readEntity(JsonObject.class);
            id = created.getString("id");
            assertThat(id).isNotBlank();
            assertThat(created.getString("workshopId")).isEqualTo(workshopId);
        }

        try (var response = this.client.byId(id)) {
            assertThat(response.getStatus()).isEqualTo(200);
            var fetched = response.readEntity(JsonObject.class);
            assertThat(fetched.getString("email")).isEqualTo("duke@airhacks.live");
        }
    }

    @Test
    void rejectRegistrationForUnknownWorkshop() {
        var unknown = this.attendee("does-not-exist");
        try (var response = this.client.register(unknown)) {
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    @Test
    void rejectRegistrationWhenWorkshopIsFull() {
        var workshopId = this.scheduleWorkshop(1);

        try (var response = this.client.register(this.attendee(workshopId))) {
            assertThat(response.getStatus()).isEqualTo(201);
        }
        try (var response = this.client.register(this.attendee(workshopId))) {
            assertThat(response.getStatus()).isEqualTo(409);
        }
    }

    @Test
    void listAttendees() {
        try (var response = this.client.all()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    void unknownAttendeeReturns404() {
        try (var response = this.client.byId("does-not-exist")) {
            assertThat(response.getStatus()).isEqualTo(404);
        }
    }
}
