package airhacks.zsmith.tools.control;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import org.json.JSONObject;

import airhacks.zsmith.configuration.control.HttpTimeouts;

public interface LinkCheckerTool {

    /// Deliberately shorter than the LLM transport timeouts: a reachability check should fail fast
    /// rather than block a tool turn. Overridable as ISO-8601 durations via `link.connect.timeout`
    /// and `link.request.timeout`. The errors — including timeouts — are returned as the tool
    /// result string, not thrown, so the model can react to them.
    Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(10);
    String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    static Duration connectTimeout() {
        return HttpTimeouts.duration("link.connect.timeout", DEFAULT_CONNECT_TIMEOUT);
    }

    static Duration requestTimeout() {
        return HttpTimeouts.duration("link.request.timeout", DEFAULT_REQUEST_TIMEOUT);
    }

    /// Built lazily so the configured connect timeout is read on first check, after config is
    /// loaded — never at interface initialization (which `create()` would trigger too early).
    static HttpClient client() {
        return Holder.instance();
    }

    final class Holder {
        private static volatile HttpClient instance;

        static HttpClient instance() {
            var current = instance;
            if (current != null) {
                return current;
            }
            synchronized (Holder.class) {
                if (instance == null) {
                    instance = HttpClient.newBuilder()
                            .connectTimeout(connectTimeout())
                            .followRedirects(Redirect.NORMAL)
                            .build();
                }
                return instance;
            }
        }
    }

    enum Field { url }

    static ToolHandler create() {
        return ToolHandler.of(
                "check_link",
                "Verifies a URL is reachable. Returns status code, final URL after redirects, and content type. Use fetch_url to retrieve page or API content.",
                ToolHandler.schema(ToolHandler.Prop.string(Field.url, "The URL to check")),
                LinkCheckerTool::run,
                true);
    }

    private static String run(JSONObject input) {
        if (!input.has(Field.url.name()) || input.getString(Field.url.name()).isBlank()) {
            return "Error: Missing required parameter: url";
        }

        var urlString = input.getString(Field.url.name());

        URI uri;
        try {
            uri = URI.create(urlString);
            if (uri.getScheme() == null) {
                return "Error: Invalid URL";
            }
        } catch (IllegalArgumentException e) {
            return "Error: Invalid URL";
        }

        try {
            var response = send(uri, "HEAD");
            if (response.statusCode() == 405 || response.statusCode() == 501) {
                response = send(uri, "GET");
            }
            return format(response);
        } catch (HttpTimeoutException e) {
            return "Error: Connection timed out";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static HttpResponse<Void> send(URI uri, String method) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(requestTimeout())
                .header("User-Agent", USER_AGENT)
                .header("Accept", "*/*")
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();
        return client().send(request, BodyHandlers.discarding());
    }

    private static String format(HttpResponse<?> response) {
        var contentType = response.headers().firstValue("Content-Type").orElse("unknown");
        return "Status: %d\nFinal-URL: %s\nContent-Type: %s"
                .formatted(response.statusCode(), response.uri(), contentType);
    }
}
