package airhacks.zsmith.tui.control;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import airhacks.zsmith.tui.entity.Config;
import airhacks.zsmith.tui.entity.Response;

public class ChatClient {

    static final String SESSION_HEADER = "X-Session-Id";

    HttpClient client;
    Config config;

    public ChatClient(Config config) {
        this.config = config;
        this.client = HttpClient.newHttpClient();
    }

    public Response chat(String sessionId, String message) {
        return send(sessionId, "/chat", message);
    }

    public Response act(String sessionId, String seed) {
        return send(sessionId, "/act", seed);
    }

    Response send(String sessionId, String path, String body) {
        var request = request(sessionId, path, body);
        try (var _ = new Spinner()) {
            var response = this.client.send(request, BodyHandlers.ofString());
            var session = response.headers().firstValue(SESSION_HEADER).orElse(sessionId);
            return new Response(response.statusCode(), response.body(), session);
        } catch (ConnectException _) {
            return new Response(-1, "Connection refused — is the server running on "
                    + this.config.host() + ":" + this.config.port() + "?", sessionId);
        } catch (IOException | InterruptedException problem) {
            return new Response(-1, message(problem), sessionId);
        }
    }

    HttpRequest request(String sessionId, String path, String body) {
        var uri = URI.create("http://" + this.config.host() + ":" + this.config.port() + path);
        var builder = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(this.config.timeout()))
                .header("Content-Type", "text/plain; charset=utf-8")
                .POST(BodyPublishers.ofString(body));
        if (sessionId != null) {
            builder.header(SESSION_HEADER, sessionId);
        }
        return builder.build();
    }

    static String message(Exception problem) {
        return problem.getMessage() != null ? problem.getMessage() : problem.getClass().getSimpleName();
    }
}
