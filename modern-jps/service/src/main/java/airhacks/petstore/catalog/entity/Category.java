package airhacks.petstore.catalog.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Category(String id, String name, String description) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("description", this.description)
                .build();
    }
}
