package airhacks.qmp.payments.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Payment(String customerId, BigDecimal amount, String description) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("customerId", this.customerId)
                .add("amount", this.amount)
                .add("description", this.description)
                .build();
    }

    public static Payment fromJSON(JsonObject json) {
        return new Payment(
                json.getString("customerId"),
                json.getJsonNumber("amount").bigDecimalValue(),
                json.getString("description"));
    }
}
