package airhacks.qmp.shipments.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Shipment(String customerId, String orderId, String address) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("customerId", this.customerId)
                .add("orderId", this.orderId)
                .add("address", this.address)
                .build();
    }

    public static Shipment fromJSON(JsonObject json) {
        return new Shipment(
                json.getString("customerId"),
                json.getString("orderId"),
                json.getString("address"));
    }
}
