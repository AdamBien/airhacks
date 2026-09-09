package airhacks.petstore.catalog.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Product(String id, String categoryId, String name, String description) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("categoryId", this.categoryId)
                .add("name", this.name)
                .add("description", this.description)
                .build();
    }

    public boolean matches(String keyword) {
        var lowered = keyword.toLowerCase();
        return this.name.toLowerCase().contains(lowered)
                || this.description.toLowerCase().contains(lowered);
    }
}
