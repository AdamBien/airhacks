package oliver;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

record PlacedOrder(long id, String customer, List<CartItem> items, double total, String timestamp) {

    static final AtomicLong ID_GEN = new AtomicLong();

    static PlacedOrder create(String customer, List<CartItem> items) {
        var total = items.stream().mapToDouble(i -> i.price() * i.quantity()).sum();
        return new PlacedOrder(ID_GEN.incrementAndGet(), customer, items, total, Instant.now().toString());
    }

    String toJson() {
        return """
                {"id":%d,"customer":"%s","items":%s,"total":%.2f,"timestamp":"%s"}""".formatted(
                id, customer, CartItem.toJsonArray(items), total, timestamp);
    }

    static String toJsonArray(List<PlacedOrder> orders) {
        var entries = orders.stream()
                .map(PlacedOrder::toJson)
                .collect(Collectors.joining(","));
        return "[" + entries + "]";
    }
}
