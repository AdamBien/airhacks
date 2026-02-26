package oliver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CustomersResource implements HttpHandler {

    final List<Customer> customers = new ArrayList<>(List.of(
            new Customer("Duke", "duke@java.net", 30, new Address("1 Java Lane", "San Francisco"),
                    List.of(new Order("Java Hat", 2), new Order("Duke Sticker", 5))),
            new Customer("James", "james@java.net", 45, new Address("2 Oak Street", "London"),
                    List.of(new Order("JDK Manual", 1)))
    ));

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            default -> exchange.sendResponseHeaders(405, -1);
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
        var response = Customer.toJsonArray(customers);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void handlePost(HttpExchange exchange) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());
        try {
            var customer = Customer.fromJson(body);
            customers.add(customer);
            var response = customer.toJson();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IllegalArgumentException e) {
            var response = """
                    {"error":"%s"}""".formatted(e.getMessage());
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
