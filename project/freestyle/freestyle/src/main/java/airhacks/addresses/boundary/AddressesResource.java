package airhacks.addresses.boundary;

import java.io.IOException;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import airhacks.addresses.control.AddressStore;
import airhacks.addresses.entity.Address;

public class AddressesResource {

    AddressStore store;

    public AddressesResource() {
        this.store = new AddressStore();
    }

    public void register(HttpServer server) {
        server.createContext("/addresses", this::handle);
    }

    void handle(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        var segments = path.split("/");

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
                    respond(exchange, 400, "{\"error\":\"street required\"}");
                }
            }
            default -> respond(exchange, 405, "{\"error\":\"method not allowed\"}");
        }
    }

    void handleFindAll(HttpExchange exchange) throws IOException {
        var json = this.store.findAll().stream()
                .map(Address::toJson)
                .collect(Collectors.joining(",", "[", "]"));
        respond(exchange, 200, json);
    }

    void handleFind(HttpExchange exchange, String street) throws IOException {
        var address = this.store.find(street);
        if (address == null) {
            respond(exchange, 404, "{\"error\":\"not found\"}");
        } else {
            respond(exchange, 200, address.toJson());
        }
    }

    void handleCreate(HttpExchange exchange) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());
        var address = Address.fromJson(body);
        this.store.create(address);
        respond(exchange, 201, address.toJson());
    }

    void handleDelete(HttpExchange exchange, String street) throws IOException {
        this.store.delete(street);
        respond(exchange, 204, "");
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
