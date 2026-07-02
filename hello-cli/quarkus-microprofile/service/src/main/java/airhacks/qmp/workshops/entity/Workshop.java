package airhacks.qmp.workshops.entity;

import java.time.LocalDate;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * An airhacks.live workshop, identified by {@code id}. {@code capacity} is the
 * maximum number of attendees.
 */
public record Workshop(String id, String title, LocalDate date, int capacity) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("title", this.title)
                .add("date", this.date.toString())
                .add("capacity", this.capacity)
                .build();
    }

    public static Workshop fromJSON(JsonObject json) {
        var id = json.getString("id", null);
        var title = json.getString("title");
        var date = LocalDate.parse(json.getString("date"));
        var capacity = json.getInt("capacity");
        return new Workshop(id, title, date, capacity);
    }
}
