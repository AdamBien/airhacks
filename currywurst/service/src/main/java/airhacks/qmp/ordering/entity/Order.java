package airhacks.qmp.ordering.entity;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/// A customer's counter order: numbered for the day, priced lines, and a state.
public class Order {

    int number;
    List<OrderLine> lines;
    OrderState state;

    public Order(int number, List<OrderLine> lines) {
        this.number = number;
        this.lines = List.copyOf(lines);
        this.state = OrderState.PLACED;
    }

    public int number() {
        return this.number;
    }

    public List<OrderLine> lines() {
        return this.lines;
    }

    public OrderState state() {
        return this.state;
    }

    public int totalInCents() {
        return this.lines.stream()
                .mapToInt(OrderLine::totalInCents)
                .sum();
    }

    public boolean isOpen() {
        return this.state.isOpen();
    }

    public boolean isCancellable() {
        return this.state == OrderState.PLACED;
    }

    /// Food already on the grill cannot be un-cooked (D3).
    public void cancel() {
        if (!isCancellable()) {
            throw new IllegalStateException("order %d is %s and can no longer be cancelled".formatted(this.number, this.state.label()));
        }
        this.state = OrderState.CANCELLED;
    }

    public JsonObject toJSON() {
        var lines = Json.createArrayBuilder();
        this.lines.stream()
                .map(OrderLine::toJSON)
                .forEach(lines::add);
        return Json.createObjectBuilder()
                .add("number", this.number)
                .add("state", this.state.label())
                .add("totalInCents", totalInCents())
                .add("lines", lines)
                .build();
    }
}
