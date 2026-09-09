package airhacks.eshop.catalog.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Product(String id, String name, int priceInCents, String color, String category, String image, boolean available) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("priceInCents", this.priceInCents)
                .add("color", this.color)
                .add("category", this.category)
                .add("available", this.available)
                .add("image", this.image)
                .build();
    }

    public static Product fromJSON(JsonObject json) {
        return new Product(
                json.getString("id"),
                json.getString("name"),
                json.getInt("priceInCents"),
                json.getString("color"),
                json.getString("category"),
                json.getString("image"),
                json.getBoolean("available"));
    }
}
