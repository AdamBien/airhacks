package airhacks.qmp.products.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Product(String productId, String name, String description, BigDecimal price, Color color) {

    public Product {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException(price);
        }
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("productId", this.productId)
                .add("name", this.name)
                .add("description", this.description)
                .add("price", this.price)
                .add("color", this.color.name().toLowerCase())
                .build();
    }

    public static Product fromJSON(JsonObject json) {
        return new Product(
                json.getString("productId"),
                json.getString("name"),
                json.getString("description"),
                json.getJsonNumber("price").bigDecimalValue(),
                Color.valueOf(json.getString("color").toUpperCase()));
    }
}
