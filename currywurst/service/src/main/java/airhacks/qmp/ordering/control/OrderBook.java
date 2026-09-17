package airhacks.qmp.ordering.control;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import airhacks.qmp.menu.control.Catalog;
import airhacks.qmp.ordering.entity.Order;
import airhacks.qmp.ordering.entity.OrderLine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

/// Today's orders, numbered from 1 in placement order (D2). Yesterday's book is dropped
/// on the first placement of a new day: a number addresses today's order only.
@ApplicationScoped
public class OrderBook {

    @Inject
    public Catalog catalog;

    public Supplier<LocalDate> today = LocalDate::now;

    LocalDate day;
    Map<Integer, Order> orders = new LinkedHashMap<>();

    public synchronized Order place(List<OrderLine> requestedLines) {
        if (requestedLines.isEmpty()) {
            throw new BadRequestException("an order needs at least one line");
        }
        var pricedLines = requestedLines.stream()
                .map(this::validateAndPrice)
                .toList();
        startNewDayIfNeeded();
        var order = new Order(this.orders.size() + 1, pricedLines);
        this.orders.put(order.number(), order);
        return order;
    }

    OrderLine validateAndPrice(OrderLine line) {
        if (!line.hasValidQuantity()) {
            throw new BadRequestException("quantity must be at least 1 for item: " + line.item());
        }
        return this.catalog.find(line.item())
                .map(item -> line.priced(item.priceInCents()))
                .orElseThrow(() -> new BadRequestException("unknown menu item: " + line.item()));
    }

    void startNewDayIfNeeded() {
        var currentDay = this.today.get();
        if (currentDay.equals(this.day)) {
            return;
        }
        this.day = currentDay;
        this.orders.clear();
    }

    public synchronized Optional<Order> find(int number) {
        return Optional.ofNullable(this.orders.get(number));
    }

    public synchronized Optional<Order> cancel(int number) {
        var order = find(number);
        order.ifPresent(Order::cancel);
        return order;
    }

    public synchronized List<Order> open() {
        return this.orders.values().stream()
                .filter(Order::isOpen)
                .toList();
    }
}
