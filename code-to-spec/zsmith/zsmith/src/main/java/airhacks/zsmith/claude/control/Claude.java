package airhacks.zsmith.claude.control;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.EnumSet;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zsmith.claude.entity.ClaudeAPICallEvent;
import airhacks.zsmith.configuration.control.HttpTimeouts;
import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.openai.control.OpenAI;



public interface Claude {

    Models defaultModel = Models.CLAUDE_48_OPUS;
    String fallbackModelName = "claude-sonnet-4-7";

    static String apiKey() {
        if (bedrock()) {
            var bedrockKey = ZCfg.string("bedrock.api.key", null);
            if (bedrockKey != null && !bedrockKey.isBlank()) {
                return bedrockKey;
            }
        }
        return ZCfg.requiredString("anthropic.api.key");
    }

    String bedrockVersion = "2023-06-01";

    static String apiVersion() {
        if (bedrock()) {
            return ZCfg.string("anthropic.version", bedrockVersion);
        }
        return ZCfg.requiredString("anthropic.version");
    }

    /// Amazon Bedrock Mantle's `bedrock-mantle` endpoint is almost entirely conventional:
    /// the only variable parts are the region, the model, and the API key. Selecting
    /// `llm.provider=bedrock` switches on convention-over-configuration — the scheme,
    /// host pattern, path, anthropic-version, and the `anthropic.` model prefix are all
    /// derived, so the same properties file can hold both the native Anthropic and the Bedrock
    /// configuration and flip between them with a single key.
    ///
    /// @see [Amazon Bedrock endpoints](https://docs.aws.amazon.com/bedrock/latest/userguide/endpoints.html)
    static boolean bedrock() {
        return "bedrock".equalsIgnoreCase(ZCfg.string("llm.provider", "claude"));
    }

    static String bedrockRegion() {
        return ZCfg.requiredString("bedrock.region");
    }

    /// A single Bedrock-namespaced project/workspace id mapped to whichever header the active
    /// wire expects — `anthropic-workspace-id` on the Messages route, `openai-project` on the
    /// Chat Completions route — so one properties file serves both Bedrock model families when
    /// the model (and thus the wire) is flipped. Used only as a fallback behind the wire-native
    /// `anthropic.workspace.id` / `openai.project` keys, mirroring how `bedrock.api.key` falls
    /// back from `anthropic.api.key`.
    static String bedrockProjectId() {
        return ZCfg.string("bedrock.project.id", null);
    }

    /// The project/workspace HTTP header carried alongside the auth credentials.
    record ProjectHeader(String name, String value) {}

    /// Resolves the project/workspace header for the given wire, or empty when unconfigured.
    /// The header *name* is dictated by the wire — Bedrock Mantle's Messages route expects
    /// `anthropic-workspace-id` and rejects `openai-project`, while its Chat Completions route
    /// expects the opposite. The *value* comes from the wire-native key
    /// (`anthropic.workspace.id` / `openai.project`), falling back to the shared
    /// [#bedrockProjectId()] so one properties file serves both Bedrock model families.
    static Optional<ProjectHeader> projectHeader(Wire wire) {
        var name = wire == Wire.OPENAI ? "openai-project" : "anthropic-workspace-id";
        var key = wire == Wire.OPENAI ? "openai.project" : "anthropic.workspace.id";
        var value = ZCfg.string(key, bedrockProjectId());
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(new ProjectHeader(name, value));
    }

    enum Capability { TEMPERATURE, EFFORT, ADAPTIVE_THINKING }

    /// The HTTP wire format a model speaks. `ANTHROPIC` is the native Messages API
    /// (`/anthropic/v1/messages`); `OPENAI` is the OpenAI-compatible Chat Completions API
    /// (`/openai/v1/chat/completions`) that Bedrock Mantle exposes for non-Anthropic models
    /// such as NVIDIA Nemotron. The protocol is a property of the model, not of the provider:
    /// on the same Bedrock Mantle host an Opus model is `ANTHROPIC` while Nemotron is `OPENAI`.
    enum Wire { ANTHROPIC, OPENAI }

    enum Models {
        NVIDIA_NEMOTRON_SUPER_3_120B("nvidia.nemotron-super-3-120b", "nvidia.nemotron-super-3-120b", 32_000, EnumSet.of(Capability.TEMPERATURE), Wire.OPENAI),
        CLAUDE_48_OPUS("claude-opus-4-8", Claude.fallbackModelName, 32_000, EnumSet.of(Capability.EFFORT, Capability.ADAPTIVE_THINKING), Wire.ANTHROPIC),
        CLAUDE_47_OPUS("claude-opus-4-7", Claude.fallbackModelName, 32_000, EnumSet.of(Capability.EFFORT, Capability.ADAPTIVE_THINKING), Wire.ANTHROPIC),
        CLAUDE_46_OPUS("claude-opus-4-6", Claude.fallbackModelName, 32_000, EnumSet.of(Capability.EFFORT, Capability.ADAPTIVE_THINKING), Wire.ANTHROPIC),
        CLAUDE_46_SONNET(Claude.fallbackModelName, Claude.fallbackModelName, 64_000, EnumSet.allOf(Capability.class), Wire.ANTHROPIC);

        private String modelName;
        private String fallbackModelName;
        private int maxTokens;
        private EnumSet<Capability> capabilities;
        private Wire wire;

        Models(String modelName, String fallbackModelName, int maxTokens, EnumSet<Capability> capabilities, Wire wire) {
            this.modelName = modelName;
            this.fallbackModelName = fallbackModelName;
            this.maxTokens = maxTokens;
            this.capabilities = capabilities;
            this.wire = wire;
        }

        public String modelName() {
            return this.modelName;
        }

        public String fallbackModelName(){
            return this.fallbackModelName;
        }

        public boolean supports(Capability capability) {
            return this.capabilities.contains(capability);
        }

        public Wire wire() {
            return this.wire;
        }

        public int maxTokens() {
            return this.maxTokens;
        }

        boolean matches(String partialName) {
            return this.modelName
                    .toLowerCase()
                    .contains(partialName.toLowerCase());
        }

        public static Models fromSystemProperty() {
            var modelInput = System.getProperty("model");
            return fromPartialMatch(modelInput)
            .orElse(defaultModel);
        }

        public static Optional<Models> fromPartialMatch(String partialName) {
            if(partialName == null)
                return Optional.empty();
            return EnumSet.allOf(Models.class)
                    .stream()
                    .filter(model -> model.matches(partialName))
                    .findAny();
         
        }
    }

    Models currentModel = selectedModel();

    /// Resolves the active model from the `claude.model` configuration first, then the `-Dmodel`
    /// system property, then the default. The configured name has to win because it drives more
    /// than the request's `model` field — it selects the wire protocol, token budget, and
    /// capabilities; deriving those from a stale default while sending a different model id is
    /// what produces "model does not support this API" errors on Bedrock Mantle.
    static Models selectedModel() {
        var configured = ZCfg.string("claude.model", null);
        return Models.fromPartialMatch(configured)
                .orElseGet(Models::fromSystemProperty);
    }

    static URI endpoint() {
        if (bedrock()) {
            var path = currentModel.wire() == Wire.OPENAI ? "v1/chat/completions" : "anthropic/v1/messages";
            return URI.create("https://bedrock-mantle.%s.api.aws/%s"
                    .formatted(bedrockRegion().trim(), path));
        }
        var scheme = ZCfg.string("claude.scheme", "https");
        var host = ZCfg.string("claude.host", "api.anthropic.com");
        var port = ZCfg.integer("claude.port", -1);
        var path = ZCfg.string("claude.path", "/v1/messages");
        var authority = port > 0 ? host + ":" + port : host;
        return URI.create("%s://%s%s".formatted(scheme, authority, path));
    }

    static String modelName() {
        var model = ZCfg.string("claude.model", currentModel.modelName());
        if (bedrock() && !model.contains(".")) {
            return "anthropic." + model;
        }
        return model;
    }

    static JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
        if (currentModel.wire() == Wire.OPENAI) {
            return invokeOpenAICompatible(system, messages, tools, temperature);
        }
        var payloadJSON = claudeMessage(messages, temperature, system);
        payloadJSON.put("model", modelName());
        if (tools != null && !tools.isEmpty()) {
            payloadJSON.put("tools", tools);
        }
        var payload = payloadJSON.toString();
        Log.request(payload);
        Log.llm(">> " + payload);
        var answer = invoke(payload);
        Log.response(answer);
        Log.llm("<< " + answer);
        return new JSONObject(answer);
    }

    /// Routes models that speak [Wire#OPENAI] (e.g. NVIDIA Nemotron on Bedrock Mantle's
    /// OpenAI-compatible Chat Completions surface) through the [OpenAI] translators so the rest
    /// of the agent stays Anthropic-native: the request is translated to OpenAI shape, sent over
    /// the same Bedrock transport as the native path, and the response is translated back to the
    /// Anthropic Messages shape callers expect.
    static JSONObject invokeOpenAICompatible(String system, JSONArray messages, JSONArray tools, float temperature) {
        var payload = OpenAI.translateRequest(system, messages, tools, temperature, modelName(), currentModel.maxTokens()).toString();
        Log.request(payload);
        Log.llm(">> " + payload);
        var answer = invoke(payload);
        var anthropicResponse = OpenAI.translateResponse(new JSONObject(answer));
        var responseString = anthropicResponse.toString();
        Log.response(responseString);
        Log.llm("<< " + responseString);
        return anthropicResponse;
    }

    public static JSONObject invoke(String system, String user, float temperature) {
        var enclosedPrompt = messagePrompt(user);
        Log.request(enclosedPrompt.toString());
        var payloadJSON = Claude.claudeMessage(enclosedPrompt, temperature, system);
        payloadJSON.put("model", modelName());
        var payload = payloadJSON.toString();
        Log.request(payload);
        Log.llm(">> " + payload);
        var answer = invoke(payload);
        Log.response(answer);
        Log.llm("<< " + answer);
        return new JSONObject(answer);
    }

    static JSONObject claudeMessage(JSONArray messages, float temperature, String system) {
        var payload = new JSONObject()
                .put("max_tokens", currentModel.maxTokens())
                .put("messages", messages)
                .put("system", system);
        if (currentModel.supports(Capability.TEMPERATURE)) {
            payload.put("temperature", temperature);
        }
        var thinking = thinkingConfig();
        if (thinking != null) {
            payload.put("thinking", thinking);
        }
        var outputConfig = outputConfig();
        if (outputConfig != null) {
            payload.put("output_config", outputConfig);
        }
        return payload;
    }

    static JSONObject thinkingConfig() {
        if (!currentModel.supports(Capability.ADAPTIVE_THINKING)) return null;
        var mode = ZCfg.string("claude.thinking", null);
        if (mode == null || mode.isBlank()) return null;
        var thinking = new JSONObject().put("type", mode);
        var display = ZCfg.string("claude.thinking.display", null);
        if (display != null && !display.isBlank() && "adaptive".equals(mode)) {
            thinking.put("display", display);
        }
        return thinking;
    }

    static JSONObject outputConfig() {
        if (!currentModel.supports(Capability.EFFORT)) return null;
        var effort = ZCfg.string("claude.effort", null);
        if (effort == null || effort.isBlank()) return null;
        return new JSONObject().put("effort", effort);
    }

    static JSONArray messagePrompt(String user) {
        return new JSONArray()
                .put(message("user", user));

    }

    static JSONObject message(String role, String content) {
        return new JSONObject()
                .put("role", role)
                .put("content", content);
    }

    /*
     * curl https://api.anthropic.com/v1/messages --header "x-api-key: YOUR_API_KEY"
     * ...
     */
    static String invoke(String message) {
        Log.agent("requesting claude model: %s".formatted(currentModel.modelName()));
        var body = sendInstrumented(message, currentModel.modelName(), false);
        if (body.statusCode() == 529) {
            Log.error("claude is overloaded, retrying with fallback model: %s".formatted(currentModel.fallbackModelName()));
            var fallbackMessage = replaceModel(message, currentModel.modelName(), currentModel.fallbackModelName());
            body = sendInstrumented(fallbackMessage, currentModel.fallbackModelName(), true);
        }
        if (body.statusCode() != 200) {
            throw new IllegalStateException("claude API error %d at %s: %s".formatted(body.statusCode(), endpoint(), body.body()));
        }
        return body.body();
    }

    static HttpResponse<String> sendInstrumented(String message, String model, boolean fallback) {
        var event = new ClaudeAPICallEvent();
        event.begin();
        var response = send(message);
        event.model = model;
        event.fallback = fallback;
        event.statusCode = response.statusCode();
        populateUsage(event, response);
        logTokens(event);
        if (event.shouldCommit()) {
            event.commit();
        }
        return response;
    }

    static void logTokens(ClaudeAPICallEvent event) {
        if (event.statusCode != 200) return;
        var max = currentModel.maxTokens();
        var headroom = max - event.outputTokens;
        Log.tokens("in=%d out=%d/%d (headroom=%d) cache_read=%d cache_create=%d".formatted(
            event.inputTokens, event.outputTokens, max, headroom,
            event.cacheReadTokens, event.cacheCreationTokens));
        if (event.outputTokens >= max * 0.9) {
            Log.warning("output tokens (%d) at %.0f%% of max (%d) — response may be truncated"
                .formatted(event.outputTokens, 100.0 * event.outputTokens / max, max));
        }
    }

    static void populateUsage(ClaudeAPICallEvent event, HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            return;
        }
        try {
            var json = new JSONObject(response.body());
            event.stopReason = json.optString("stop_reason", null);
            var servedModel = json.optString("model", null);
            if (servedModel != null && !servedModel.isBlank()) {
                Log.agent("served by claude model: %s".formatted(servedModel));
                event.model = servedModel;
            }
            var usage = json.optJSONObject("usage");
            if (usage != null && currentModel.wire() == Wire.OPENAI) {
                event.inputTokens = usage.optInt("prompt_tokens");
                event.outputTokens = usage.optInt("completion_tokens");
            } else if (usage != null) {
                event.inputTokens = usage.optInt("input_tokens");
                event.outputTokens = usage.optInt("output_tokens");
                event.cacheReadTokens = usage.optInt("cache_read_input_tokens");
                event.cacheCreationTokens = usage.optInt("cache_creation_input_tokens");
            }
        } catch (RuntimeException ignored) {
        }
    }

    static String replaceModel(String message, String originalModel, String fallbackModel) {
        return message.replace(originalModel, fallbackModel);
    }

    static HttpResponse<String> send(String message) {
        var uri = endpoint();
        Log.agent("claude endpoint: " + uri);
        var builder = HttpRequest.newBuilder(uri)
                .timeout(HttpTimeouts.requestTimeout())
                .POST(BodyPublishers.ofString(message))
                .header("content-type", "application/json");
        if (currentModel.wire() == Wire.OPENAI) {
            builder.header("Authorization", "Bearer " + apiKey());
        } else {
            var authHeader = ZCfg.string("anthropic.auth.header", "x-api-key");
            builder.header(authHeader, apiKey())
                    .header("anthropic-version", apiVersion());
        }
        projectHeader(currentModel.wire())
                .ifPresent(header -> builder.header(header.name(), header.value()));
        var request = builder.build();
        try {
            return HttpTimeouts.client().send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Log.error(e.getMessage());
            throw new IllegalStateException("cannot communicate with claude", e);
        }
    }

}
