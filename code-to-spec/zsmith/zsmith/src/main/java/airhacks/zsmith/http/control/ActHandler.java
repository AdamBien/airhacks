package airhacks.zsmith.http.control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import airhacks.zsmith.http.boundary.ChatEngine;
import airhacks.zsmith.logging.control.Log;

public record ActHandler(ChatEngine engine, Sessions sessions) implements HttpHandler {

    static final String DEFAULT_SEED = "go";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Exchanges.sendPlain(exchange, 405, "Method not allowed — use POST");
                return;
            }
            var body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            var seed = body.isEmpty() ? DEFAULT_SEED : body;
            var sessionId = this.sessions.resolveOrCreate(exchange.getRequestHeaders().getFirst(Sessions.HEADER));
            exchange.getResponseHeaders().add(Sessions.HEADER, sessionId);
            var lock = this.sessions.lockFor(sessionId);
            lock.lock();
            try {
                var response = this.engine.chat(sessionId, seed);
                Exchanges.sendPlain(exchange, 200, response == null ? "" : response);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            Log.error("act handler error: " + e.getMessage(), e);
            Exchanges.sendPlain(exchange, 500, e.getMessage() == null ? "Internal error" : e.getMessage());
        }
    }
}
