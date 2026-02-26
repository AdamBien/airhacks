package airhacks.customers.boundary;

import java.io.IOException;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import airhacks.addresses.entity.Address;
import airhacks.customers.control.CustomerStore;
import airhacks.customers.entity.Customer;

public class CustomersResource {

    CustomerStore store;

    public CustomersResource() {
        this.store = new CustomerStore();
    }

    public void register(HttpServer server) {
        server.createContext("/customers", this::handle);
    }

    void handle(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        var segments = path.split("/");

        // /customers/{name}/addresses or /customers/{name}/addresses/{street}
        if (segments.length >= 4 && "addresses".equals(segments[3])) {
            handleAddresses(exchange, method, segments);
            return;
        }

        switch (method) {
            case "GET" -> {
                if (segments.length > 2) {
                    handleFind(exchange, segments[2]);
                } else {
                    handleFindAll(exchange);
                }
            }
            case "POST" -> handleCreate(exchange);
            case "DELETE" -> {
                if (segments.length > 2) {
                    handleDelete(exchange, segments[2]);
                } else {
                    respond(exchange, 400, "{\"error\":\"name required\"}");
                }
            }
            default -> respond(exchange, 405, "{\"error\":\"method not allowed\"}");
        }
    }

    void handleFindAll(HttpExchange exchange) throws IOException {
        var json = this.store.findAll().stream()
                .map(Customer::toJson)
                .collect(Collectors.joining(",", "[", "]"));
        respond(exchange, 200, json);
    }

    void handleFind(HttpExchange exchange, String name) throws IOException {
        var customer = this.store.find(name);
        if (customer == null) {
            respond(exchange, 404, "{\"error\":\"not found\"}");
        } else {
            respond(exchange, 200, customer.toJson());
        }
    }

    void handleCreate(HttpExchange exchange) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());
        var customer = Customer.fromJson(body);
        this.store.create(customer);
        respond(exchange, 201, customer.toJson());
    }

    void handleDelete(HttpExchange exchange, String name) throws IOException {
        this.store.delete(name);
        respond(exchange, 204, "");
    }

    void handleAddresses(HttpExchange exchange, String method, String[] segments) throws IOException {
        var name = segments[2];
        var customer = this.store.find(name);
        if (customer == null) {
            respond(exchange, 404, "{\"error\":\"customer not found\"}");
            return;
        }
        switch (method) {
            case "GET" -> {
                var json = customer.addresses().stream()
                        .map(Address::toJson)
                        .collect(Collectors.joining(",", "[", "]"));
                respond(exchange, 200, json);
            }
            case "POST" -> {
                var body = new String(exchange.getRequestBody().readAllBytes());
                var address = Address.fromJson(body);
                var updated = this.store.addAddress(name, address);
                respond(exchange, 201, updated.toJson());
            }
            case "DELETE" -> {
                if (segments.length > 4) {
                    this.store.removeAddress(name, segments[4]);
                    respond(exchange, 204, "");
                } else {
                    respond(exchange, 400, "{\"error\":\"street required\"}");
                }
            }
            default -> respond(exchange, 405, "{\"error\":\"method not allowed\"}");
        }
    }

    void respond(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        var bytes = body.getBytes();
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        if (bytes.length > 0) {
            try (var os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
