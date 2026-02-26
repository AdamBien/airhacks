package oliver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ProductsResource implements HttpHandler {

    final List<Product> products = new ArrayList<>(List.of(
            Product.create("Java Hat", "Classic duke hat for Java developers", 29.99),
            Product.create("JDK Manual", "Complete guide to the Java Development Kit", 49.99),
            Product.create("Duke Sticker Pack", "Set of 10 Duke stickers", 9.99),
            Product.create("Coffee Mug", "Write once, run anywhere mug", 14.99),
            Product.create("Lambda T-Shirt", "Functional style t-shirt", 24.99)
    ));

    Optional<Product> findById(long id) {
        return products.stream().filter(p -> p.id() == id).findFirst();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            default -> exchange.sendResponseHeaders(405, -1);
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
        var response = Product.toJsonArray(products);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void handlePost(HttpExchange exchange) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());
        var product = Product.fromJson(body);
        products.add(product);
        var response = product.toJson();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
