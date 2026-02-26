package airhacks.qmp.shoppingcarts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;

import java.util.List;

public record ShoppingCart(String customerId, List<Item> items) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("customerId", this.customerId)
                .add("items", this.items.stream()
                        .map(Item::toJSON)
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }

    public static ShoppingCart fromJSON(JsonObject json) {
        var items = json.getJsonArray("items").stream()
                .map(v -> Item.fromJSON(v.asJsonObject()))
                .toList();
        return new ShoppingCart(
                json.getString("customerId"),
                items);
    }
}
