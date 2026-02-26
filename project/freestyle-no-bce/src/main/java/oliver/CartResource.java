package oliver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CartResource implements HttpHandler {

    final Map<String, List<CartItem>> carts = new HashMap<>();
    final ProductsResource productsResource;

    CartResource(ProductsResource productsResource) {
        this.productsResource = productsResource;
    }

    List<CartItem> getCart(String customer) {
        return carts.computeIfAbsent(customer, _ -> new ArrayList<>());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var query = exchange.getRequestURI().getQuery();
        var params = parseQuery(query);
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
            case "DELETE" -> handleDelete(exchange, customer, params);
            default -> exchange.sendResponseHeaders(405, -1);
        }
    }

    void handleGet(HttpExchange exchange, String customer) throws IOException {
        var cart = getCart(customer);
        var response = CartItem.toJsonArray(cart);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void handlePost(HttpExchange exchange, String customer) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());
        var productId = Long.parseLong(JsonUtil.extract(body, "productId"));
        var quantity = Integer.parseInt(JsonUtil.extract(body, "quantity"));
        var product = productsResource.findById(productId);
        if (product.isEmpty()) {
            var response = """
                    {"error":"product not found"}""";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        var p = product.get();
        var cart = getCart(customer);
        var existing = cart.stream().filter(i -> i.productId() == productId).findFirst();
        if (existing.isPresent()) {
            cart.remove(existing.get());
            cart.add(new CartItem(productId, p.name(), existing.get().quantity() + quantity, p.price()));
        } else {
            cart.add(new CartItem(productId, p.name(), quantity, p.price()));
        }
        var response = CartItem.toJsonArray(cart);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void handleDelete(HttpExchange exchange, String customer, Map<String, String> params) throws IOException {
        var productIdStr = params.get("productId");
        if (productIdStr == null) {
            var response = """
                    {"error":"productId parameter is required"}""";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, response.length());
            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }
        var productId = Long.parseLong(productIdStr);
        var cart = getCart(customer);
        cart.removeIf(i -> i.productId() == productId);
        var response = CartItem.toJsonArray(cart);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    static Map<String, String> parseQuery(String query) {
        var params = new HashMap<String, String>();
        if (query == null) return params;
        for (var pair : query.split("&")) {
            var kv = pair.split("=", 2);
            if (kv.length == 2) {
                params.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return params;
    }
}
