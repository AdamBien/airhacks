package airhacks.qmp.shoppingcarts.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Item(String product, int quantity, BigDecimal price) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("product", this.product)
                .add("quantity", this.quantity)
                .add("price", this.price)
                .build();
    }

    public static Item fromJSON(JsonObject json) {
        return new Item(
                json.getString("product"),
                json.getInt("quantity"),
                json.getJsonNumber("price").bigDecimalValue());
    }
}
