package airhacks.zsmith.lightmetal.control;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.UnaryOperator;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.lightmetal.entity.LightMetalAPICallEvent;
import airhacks.zsmith.logging.control.Log;

public interface LightMetal {

    String MODEL_PROPERTY = "lightmetal.model";
    String MAX_TOKENS_PROPERTY = "lightmetal.max.tokens";
    int DEFAULT_MAX_TOKENS = 4096;

    static Optional<String> modelOverride() {
        var value = ZCfg.string(MODEL_PROPERTY);
        return (value == null || value.isBlank()) ? Optional.empty() : Optional.of(value);
    }

    static int maxTokens() {
        return ZCfg.integer(MAX_TOKENS_PROPERTY, DEFAULT_MAX_TOKENS);
    }

    static boolean available() {
        return ChatHolder.lookup() != null;
    }

    static JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
        var payload = anthropicPayload(system, messages, tools, temperature);
        var payloadString = payload.toString();
        Log.request(payloadString);
        Log.llm(">> " + payloadString);

        var event = new LightMetalAPICallEvent();
        event.begin();
        event.model = payload.optString("model", "(from lightmetal config)");
        Log.agent("invoking lightmetal model: " + event.model);

        var chat = ChatHolder.lookup();
        if (chat == null) {
            throw new IllegalStateException(
                    "no UnaryOperator<String> service found — add lightmetal.jar to the classpath "
                            + "(see https://github.com/AdamBien/lightmetal)");
        }
        var answer = chat.apply(payloadString);
        var response = new JSONObject(answer);
        event.model = response.optString("model", event.model);
        populateUsage(event, response);
        logTokens(event);
        if (event.shouldCommit()) {
            event.commit();
        }
        Log.response(answer);
        Log.llm("<< " + answer);
        return response;
    }

    static JSONObject invoke(String system, String user, float temperature) {
        var messages = new JSONArray()
                .put(new JSONObject().put("role", "user").put("content", user));
        return invoke(system, messages, null, temperature);
    }

    static JSONObject anthropicPayload(String system, JSONArray messages, JSONArray tools, float temperature) {
        var payload = new JSONObject()
                .put("system", system == null ? "" : system)
                .put("messages", messages)
                .put("max_tokens", maxTokens())
                .put("temperature", temperature);
        // Omit `model` in the default case so lightmetal owns it via its own ZCfg.
        // Only include it when zsmith is explicitly overriding (e.g. running a
        // different GGUF for one agent without touching ~/.lightmetal/app.properties).
        modelOverride().ifPresent(override -> payload.put("model", override));
        if (tools != null && !tools.isEmpty()) {
            payload.put("tools", tools);
        }
        return payload;
    }

    static void populateUsage(LightMetalAPICallEvent event, JSONObject response) {
        event.stopReason = response.optString("stop_reason", null);
        var usage = response.optJSONObject("usage");
        if (usage != null) {
            event.inputTokens = usage.optInt("input_tokens");
            event.outputTokens = usage.optInt("output_tokens");
        }
    }

    static void logTokens(LightMetalAPICallEvent event) {
        var max = maxTokens();
        var headroom = max - event.outputTokens;
        Log.tokens("in=%d out=%d/%d (headroom=%d)".formatted(
                event.inputTokens, event.outputTokens, max, headroom));
        if (event.outputTokens >= max * 0.9) {
            Log.warning("output tokens (%d) at %.0f%% of max (%d) — response may be truncated"
                    .formatted(event.outputTokens, 100.0 * event.outputTokens / max, max));
        }
    }

    final class ChatHolder {

        static volatile UnaryOperator<String> instance;
        static volatile boolean discovered;

        static UnaryOperator<String> lookup() {
            if (discovered) return instance;
            synchronized (ChatHolder.class) {
                if (!discovered) {
                    instance = discover();
                    discovered = true;
                }
                return instance;
            }
        }

        @SuppressWarnings("unchecked")
        static UnaryOperator<String> discover() {
            var found = ServiceLoader.load(UnaryOperator.class).findFirst().orElse(null);
            if (found == null) return null;
            if (found instanceof AutoCloseable closeable) {
                registerShutdownHook(closeable);
            }
            Log.agent("lightmetal provider discovered: " + found.getClass().getName());
            return (UnaryOperator<String>) found;
        }

        static void registerShutdownHook(AutoCloseable closeable) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> closeQuietly(closeable), "lightmetal-shutdown"));
        }

        static void closeQuietly(AutoCloseable closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                Log.error("lightmetal close failed", e);
            }
        }
    }
}
