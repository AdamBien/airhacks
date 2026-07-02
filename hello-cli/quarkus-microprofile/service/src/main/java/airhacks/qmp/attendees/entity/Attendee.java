package airhacks.qmp.attendees.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * An airhacks.live workshop attendee, identified by {@code id} and enrolled in
 * the workshop referenced by {@code workshopId}.
 */
public record Attendee(String id, String name, String email, String workshopId) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("email", this.email)
                .add("workshopId", this.workshopId)
                .build();
    }

    public static Attendee fromJSON(JsonObject json) {
        var id = json.getString("id", null);
        var name = json.getString("name");
        var email = json.getString("email");
        var workshopId = json.getString("workshopId", null);
        return new Attendee(id, name, email, workshopId);
    }
}
