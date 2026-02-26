package oliver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OrdersResource implements HttpHandler {

    final Map<String, List<PlacedOrder>> orders = new HashMap<>();
    final CartResource cartResource;

    OrdersResource(CartResource cartResource) {
        this.cartResource = cartResource;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var query = exchange.getRequestURI().getQuery();
        var params = CartResource.parseQuery(query);
        var customer = params.get("customer");
        if (customer == null || customer.isBlank()) {
            var response = """
                    {"error":"customer parameter is required"}""";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGet(exchange, customer);
            case "POST" -> handlePost(exchange, customer);
            default -> exchange.sendResponseHeaders(405, -1);
        }
    }

    void handleGet(HttpExchange exchange, String customer) throws IOException {
        var customerOrders = orders.getOrDefault(customer, List.of());
        var response = PlacedOrder.toJsonArray(customerOrders);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void handlePost(HttpExchange exchange, String customer) throws IOException {
        var cart = cartResource.getCart(customer);
        if (cart.isEmpty()) {
            var response = """
                    {"error":"cart is empty"}""";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        var order = PlacedOrder.create(customer, List.copyOf(cart));
        orders.computeIfAbsent(customer, _ -> new ArrayList<>()).add(order);
        cart.clear();
        var response = order.toJson();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
