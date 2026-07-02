package airhacks.qmp.sessions.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * A session or talk within an airhacks.live workshop, identified by {@code id}.
 * {@code workshopId} links the session to its parent workshop.
 */
public record Session(String id, String title, String speaker, int duration, String workshopId) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("title", this.title)
                .add("speaker", this.speaker)
                .add("duration", this.duration)
                .add("workshopId", this.workshopId)
                .build();
    }

    public static Session fromJSON(JsonObject json) {
        var id = json.getString("id", null);
        var title = json.getString("title");
        var speaker = json.getString("speaker");
        var duration = json.getInt("duration");
        var workshopId = json.getString("workshopId");
        return new Session(id, title, speaker, duration, workshopId);
    }
}
