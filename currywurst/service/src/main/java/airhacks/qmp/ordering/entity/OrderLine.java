package airhacks.qmp.ordering.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/// One menu item and its quantity within an order, priced in whole cents.
public record OrderLine(String item, int quantity, int unitPriceInCents) {

    static final int UNPRICED = 0;

    /// A line as requested at the counter: not yet validated or priced.
    public static OrderLine requested(String item, int quantity) {
        return new OrderLine(item, quantity, UNPRICED);
    }

    public OrderLine priced(int unitPriceInCents) {
        return new OrderLine(this.item, this.quantity, unitPriceInCents);
    }

    public boolean hasValidQuantity() {
        return this.quantity >= 1;
    }

    public int totalInCents() {
        return this.unitPriceInCents * this.quantity;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("item", this.item)
                .add("quantity", this.quantity)
                .add("unitPriceInCents", this.unitPriceInCents)
                .add("totalInCents", totalInCents())
                .build();
    }

    /// Missing fields map to values the validation rejects, never to an exception.
    public static OrderLine fromJSON(JsonObject json) {
        return new OrderLine(
                json.getString("item", ""),
                json.getInt("quantity", 0),
                json.getInt("unitPriceInCents", UNPRICED));
    }
}
