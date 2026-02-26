package airhacks.qmp.orders.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Order(String customerId, String product, int quantity, BigDecimal price) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("customerId", this.customerId)
                .add("product", this.product)
                .add("quantity", this.quantity)
                .add("price", this.price)
                .build();
    }

    public static Order fromJSON(JsonObject json) {
        return new Order(
                json.getString("customerId"),
                json.getString("product"),
                json.getInt("quantity"),
                json.getJsonNumber("price").bigDecimalValue());
    }
}
