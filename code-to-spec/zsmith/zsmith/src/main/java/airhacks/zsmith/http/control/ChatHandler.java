package airhacks.zsmith.http.control;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import airhacks.zsmith.http.boundary.ChatEngine;
import airhacks.zsmith.logging.control.Log;

public record ChatHandler(ChatEngine engine, Sessions sessions) implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Exchanges.sendPlain(exchange, 405, "Method not allowed — use POST");
                return;
            }
            var body = new String(exchange.getRequestBody().readAllBytes());
            if (body.isEmpty()) {
                Exchanges.sendPlain(exchange, 400, "Request body must not be empty");
                return;
            }
            var sessionIdHeader = exchange.getRequestHeaders().getFirst(Sessions.HEADER);
            var sessionId = this.sessions.resolveOrCreate(sessionIdHeader);
            exchange.getResponseHeaders().add(Sessions.HEADER, sessionId);
            var lock = this.sessions.lockFor(sessionId);
            lock.lock();
            try {
                var response = this.engine.chat(sessionId, body);
                Exchanges.sendPlain(exchange, 200, response == null ? "" : response);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            Log.error("chat handler error: " + e.getMessage(), e);
            Exchanges.sendPlain(exchange, 500, e.getMessage() == null ? "Internal error" : e.getMessage());
        }
    }
}
