package airhacks.contacts.contacts.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@QuarkusTest
class ContactsResourceIT {

    static final String UNKNOWN_ID = "unknown-such-contact";

    @Inject
    @RestClient
    ContactsResourceClient client;

    static JsonObject contact(String firstName, String lastName, String email, String phone) {
        return contact(firstName, lastName, email, phone, "private");
    }

    static JsonObject contact(String firstName, String lastName, String email, String phone, String type) {
        var builder = Json.createObjectBuilder();
        if (firstName != null) {
            builder.add("firstName", firstName);
        }
        if (lastName != null) {
            builder.add("lastName", lastName);
        }
        if (email != null) {
            builder.add("email", email);
        }
        if (phone != null) {
            builder.add("phone", phone);
        }
        if (type != null) {
            builder.add("type", type);
        }
        return builder.build();
    }

    String createAndReturnId(JsonObject payload) {
        try (var response = this.client.createContact(payload)) {
            assertThat(response.getStatus()).isEqualTo(201);
            return response.readEntity(JsonObject.class).getString("id");
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("createContactCases")
    void createContact(String requirement, JsonObject payload, int expectedStatus) {
        try (var response = this.client.createContact(payload)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (expectedStatus != 201) {
                return;
            }
            var created = response.readEntity(JsonObject.class);
            var id = created.getString("id");
            assertThat(id).as(requirement + " assigns a unique id").isNotBlank();
            assertThat(created.getString("lastName")).as(requirement + " returns the stored contact")
                    .isEqualTo(payload.getString("lastName"));
            try (var lookup = this.client.getContact(id)) {
                assertThat(lookup.getStatus()).as(requirement + " stores the contact").isEqualTo(200);
            }
        }
    }

    static Stream<Arguments> createContactCases() {
        return Stream.of(
                arguments("R1.1", contact("duke", "java", "duke@java.net", "+1 555 0100"), 201),
                arguments("R1.2", contact("tux", "penguin", "tux@kernel.org", null), 201),
                arguments("R1.3", contact("duke", null, "duke@java.net", null), 400),
                arguments("R1.4", contact("duke", "java", "not-an-email", null), 400));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("contactTypeCases")
    void contactType(String requirement, String type, int expectedStatus) {
        var payload = contact("duke", "typed", "duke@types.dev", null, type);
        try (var response = this.client.createContact(payload)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (expectedStatus != 201) {
                return;
            }
            var created = response.readEntity(JsonObject.class);
            assertThat(created.getString("type")).as(requirement + " stores the type").isEqualTo(type);
        }
    }

    static Stream<Arguments> contactTypeCases() {
        return Stream.of(
                arguments("R5.1", "business", 201),
                arguments("R5.2", null, 400),
                arguments("R5.3", "imaginary", 400));
    }

    @Test
    void listContacts() {
        var firstId = this.createAndReturnId(contact("juggy", "jug", "juggy@jug.org", null));
        var secondId = this.createAndReturnId(contact("moby", "dock", "moby@docker.io", null));
        try (var response = this.client.listContacts()) {
            assertThat(response.getStatus()).as("R2.1").isEqualTo(200);
            var contacts = response.readEntity(jakarta.json.JsonArray.class);
            var ids = contacts.stream()
                    .map(JsonObject.class::cast)
                    .map(entry -> entry.getString("id"))
                    .toList();
            assertThat(ids).as("R2.1 provides all stored contacts").contains(firstId, secondId);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getContactCases")
    void getContact(String requirement, boolean stored, int expectedStatus) {
        var payload = contact("kubey", "cattle", "kubey@k8s.io", null);
        var id = stored ? this.createAndReturnId(payload) : UNKNOWN_ID;
        try (var response = this.client.getContact(id)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (expectedStatus == 200) {
                var found = response.readEntity(JsonObject.class);
                assertThat(found.getString("lastName")).as(requirement + " returns the contact")
                        .isEqualTo(payload.getString("lastName"));
            }
        }
    }

    static Stream<Arguments> getContactCases() {
        return Stream.of(
                arguments("R2.2", true, 200),
                arguments("R2.3", false, 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("updateContactCases")
    void updateContact(String requirement, boolean stored, JsonObject update, int expectedStatus) {
        var id = stored ? this.createAndReturnId(contact("lambda", "closure", "lambda@jdk.dev", null)) : UNKNOWN_ID;
        try (var response = this.client.updateContact(id, update)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (expectedStatus != 200) {
                return;
            }
            try (var lookup = this.client.getContact(id)) {
                var updated = lookup.readEntity(JsonObject.class);
                assertThat(updated.getString("lastName")).as(requirement + " replaces the stored fields")
                        .isEqualTo(update.getString("lastName"));
                assertThat(updated.getString("email")).as(requirement + " replaces the stored fields")
                        .isEqualTo(update.getString("email"));
            }
        }
    }

    static Stream<Arguments> updateContactCases() {
        return Stream.of(
                arguments("R3.1", true, contact("streams", "gatherer", "gatherer@jdk.dev", "+1 555 0123"), 200),
                arguments("R3.2", false, contact("valid", "update", "valid@update.dev", null), 404),
                arguments("R3.3", true, contact("duke", null, "not-an-email", null), 400));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deleteContactCases")
    void deleteContact(String requirement, boolean stored, int expectedStatus) {
        var id = stored ? this.createAndReturnId(contact("garbage", "collector", "zgc@jdk.dev", null)) : UNKNOWN_ID;
        try (var response = this.client.deleteContact(id)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        }
        if (expectedStatus == 204) {
            try (var lookup = this.client.getContact(id)) {
                assertThat(lookup.getStatus()).as(requirement + " removes the contact").isEqualTo(404);
            }
        }
    }

    static Stream<Arguments> deleteContactCases() {
        return Stream.of(
                arguments("R4.1", true, 204),
                arguments("R4.2", false, 404));
    }
}
