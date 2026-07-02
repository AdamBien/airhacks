package airhacks.zsmith.episodicmemory.entity;

import java.time.Instant;

import org.json.JSONObject;

public record Episode(String content, String timestamp, MemoryType type) {

    public Episode {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Episode content must not be empty");
        }
        if (timestamp == null) {
            timestamp = Instant.now().toString();
        }
    }

    public static Episode of(String content) {
        return new Episode(content, null, null);
    }

    public static Episode of(String content, MemoryType type) {
        return new Episode(content, null, type);
    }

    public boolean hasType(MemoryType type) {
        return this.type != null && this.type.equals(type);
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .put("content", this.content)
                .put("timestamp", this.timestamp)
                .put("type", this.type == null ? JSONObject.NULL : this.type.name());
    }

    public static Episode fromJSON(JSONObject json) {
        var content = json.getString("content");
        var timestamp = json.getString("timestamp");
        String typeString = null;
        if (json.has("type") && !json.isNull("type")) {
            typeString = json.getString("type");
        } else if (json.has("category") && !json.isNull("category")) {
            typeString = json.getString("category");
        }
        var type = MemoryType.fromString(typeString);
        return new Episode(content, timestamp, type);
    }
}
