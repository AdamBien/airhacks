package airhacks.zsmith.configuration.control;

import java.net.http.HttpClient;
import java.time.Duration;

/// Shared, configuration-driven HTTP timeouts for outbound LLM API calls.
///
/// Without these, [HttpClient#send] blocks indefinitely when a provider stalls
/// mid-response — turning a transient slowdown into a permanently hung agent loop
/// (the failure observed against Bedrock Mantle's Nemotron endpoint). With them,
/// a stall surfaces as an [java.net.http.HttpTimeoutException] the transport already
/// wraps into an [IllegalStateException], so the loop fails fast instead of hanging.
///
/// Defaults are deliberately generous on the request side: reasoning models can take
/// minutes to produce a full, non-streamed completion. Both are overridable via config
/// as ISO-8601 durations (the format [Duration#parse] accepts), e.g. `PT10S`, `PT5M`:
///
///   http.connect.timeout (default PT10S)
///   http.request.timeout (default PT5M)
///
/// The client is built lazily and memoized so class initialization never depends on
/// [ZCfg#loadBaseConfig] having run first — the timeouts are resolved on first send.
public interface HttpTimeouts {

    Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofMinutes(5);

    static Duration connectTimeout() {
        return duration("http.connect.timeout", DEFAULT_CONNECT_TIMEOUT);
    }

    static Duration requestTimeout() {
        return duration("http.request.timeout", DEFAULT_REQUEST_TIMEOUT);
    }

    static Duration duration(String key, Duration fallback) {
        var value = ZCfg.string(key, null);
        return (value == null || value.isBlank()) ? fallback : Duration.parse(value.trim());
    }

    /// The shared, connect-timeout-configured HTTP client, built on first call.
    /// Apply [#requestTimeout()] per request via [java.net.http.HttpRequest.Builder#timeout].
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
                            .build();
                }
                return instance;
            }
        }
    }
}
