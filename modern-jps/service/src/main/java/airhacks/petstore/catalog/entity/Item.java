package airhacks.petstore.catalog.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Item(String id, String productId, String name, BigDecimal listPrice) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("productId", this.productId)
                .add("name", this.name)
                .add("listPrice", this.listPrice)
                .build();
    }
}
