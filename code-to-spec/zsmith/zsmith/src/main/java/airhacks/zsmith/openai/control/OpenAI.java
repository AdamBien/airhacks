package airhacks.zsmith.openai.control;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zsmith.configuration.control.HttpTimeouts;
import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.openai.entity.OpenAIAPICallEvent;

public interface OpenAI {

    static URI endpoint() {
        var scheme = ZCfg.string("openai.scheme", "https");
        var host = ZCfg.string("openai.host", "api.openai.com");
        var port = ZCfg.integer("openai.port", -1);
        var authority = port > 0 ? host + ":" + port : host;
        return URI.create("%s://%s/v1/chat/completions".formatted(scheme, authority));
    }

    static String model() {
        return ZCfg.string("openai.model", "gpt-4o");
    }

    static int maxTokens() {
        return ZCfg.integer("openai.max.tokens", 4096);
    }

    static String apiKey() {
        return ZCfg.string("openai.api.key", "");
    }

    static JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
        var openaiPayload = translateRequest(system, messages, tools, temperature);
        var payloadString = openaiPayload.toString();
        Log.request(payloadString);
        Log.llm(">> " + payloadString);

        var event = new OpenAIAPICallEvent();
        event.begin();
        event.model = model();
        var response = send(payloadString);
        event.statusCode = response.statusCode();

        if (response.statusCode() != 200) {
            var message = extractErrorMessage(response.body());
            throw new IllegalStateException(
                "openai API error %d: %s".formatted(response.statusCode(), message));
        }

        var openaiResponse = new JSONObject(response.body());
        populateUsage(event, openaiResponse);
        var anthropicResponse = translateResponse(openaiResponse);
        event.stopReason = anthropicResponse.optString("stop_reason", null);
        logTokens(event);
        if (event.shouldCommit()) {
            event.commit();
        }

        var anthropicString = anthropicResponse.toString();
        Log.response(anthropicString);
        Log.llm("<< " + anthropicString);
        return anthropicResponse;
    }

    static JSONObject translateRequest(String system, JSONArray messages, JSONArray tools, float temperature) {
        return translateRequest(system, messages, tools, temperature, model(), maxTokens());
    }

    static JSONObject translateRequest(String system, JSONArray messages, JSONArray tools, float temperature, String model, int maxTokens) {
        var openaiMessages = new JSONArray();
        if (system != null && !system.isBlank()) {
            openaiMessages.put(new JSONObject().put("role", "system").put("content", system));
        }
        for (var i = 0; i < messages.length(); i++) {
            translateMessage(messages.getJSONObject(i), openaiMessages);
        }

        var payload = new JSONObject()
                .put("model", model)
                .put("messages", openaiMessages)
                .put("temperature", temperature)
                .put("max_tokens", maxTokens);

        if (tools != null && !tools.isEmpty()) {
            payload.put("tools", translateTools(tools));
        }
        return payload;
    }

    static void translateMessage(JSONObject anthropicMessage, JSONArray openaiMessages) {
        var role = anthropicMessage.getString("role");
        var content = anthropicMessage.get("content");

        if (content instanceof String s) {
            openaiMessages.put(new JSONObject().put("role", role).put("content", s));
            return;
        }
        if (!(content instanceof JSONArray blocks)) {
            openaiMessages.put(new JSONObject().put("role", role).put("content", content.toString()));
            return;
        }

        if ("user".equals(role)) {
            for (var i = 0; i < blocks.length(); i++) {
                var block = blocks.getJSONObject(i);
                if ("tool_result".equals(block.optString("type"))) {
                    var toolMessage = new JSONObject()
                            .put("role", "tool")
                            .put("tool_call_id", block.getString("tool_use_id"))
                            .put("content", toolResultContent(block));
                    openaiMessages.put(toolMessage);
                }
            }
            return;
        }

        // assistant: split blocks into text content + tool_calls
        var textParts = new StringBuilder();
        var toolCalls = new JSONArray();
        for (var i = 0; i < blocks.length(); i++) {
            var block = blocks.getJSONObject(i);
            var type = block.optString("type");
            if ("text".equals(type)) {
                if (textParts.length() > 0) textParts.append("\n");
                textParts.append(block.optString("text", ""));
            } else if ("tool_use".equals(type)) {
                var input = block.optJSONObject("input");
                if (input == null) input = new JSONObject();
                var call = new JSONObject()
                        .put("id", block.getString("id"))
                        .put("type", "function")
                        .put("function", new JSONObject()
                                .put("name", block.getString("name"))
                                .put("arguments", input.toString()));
                toolCalls.put(call);
            }
        }
        var assistantMessage = new JSONObject().put("role", "assistant");
        if (textParts.length() > 0) {
            assistantMessage.put("content", textParts.toString());
        } else {
            assistantMessage.put("content", JSONObject.NULL);
        }
        if (!toolCalls.isEmpty()) {
            assistantMessage.put("tool_calls", toolCalls);
        }
        openaiMessages.put(assistantMessage);
    }

    static String toolResultContent(JSONObject block) {
        var content = block.optString("content", "");
        return block.optBoolean("is_error", false) ? "ERROR: " + content : content;
    }

    static JSONArray translateTools(JSONArray anthropicTools) {
        var openaiTools = new JSONArray();
        for (var i = 0; i < anthropicTools.length(); i++) {
            var t = anthropicTools.getJSONObject(i);
            var function = new JSONObject()
                    .put("name", t.getString("name"))
                    .put("description", t.optString("description", ""))
                    .put("parameters", t.optJSONObject("input_schema", new JSONObject()));
            openaiTools.put(new JSONObject().put("type", "function").put("function", function));
        }
        return openaiTools;
    }

    static JSONObject translateResponse(JSONObject openaiResponse) {
        var choices = openaiResponse.optJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalStateException("openai response missing choices: " + openaiResponse);
        }
        var choice = choices.getJSONObject(0);
        var message = choice.getJSONObject("message");
        var finishReason = choice.optString("finish_reason", "stop");

        var contentBlocks = new JSONArray();
        var text = message.optString("content", "");
        if (text != null && !text.isEmpty() && !JSONObject.NULL.equals(message.opt("content"))) {
            contentBlocks.put(new JSONObject().put("type", "text").put("text", text));
        }
        var toolCalls = message.optJSONArray("tool_calls");
        if (toolCalls != null) {
            for (var i = 0; i < toolCalls.length(); i++) {
                var call = toolCalls.getJSONObject(i);
                var function = call.getJSONObject("function");
                contentBlocks.put(new JSONObject()
                        .put("type", "tool_use")
                        .put("id", call.getString("id"))
                        .put("name", function.getString("name"))
                        .put("input", parseArgs(function.optString("arguments", ""))));
            }
        }

        var stopReason = "tool_calls".equals(finishReason) ? "tool_use" : "end_turn";
        return new JSONObject()
                .put("content", contentBlocks)
                .put("stop_reason", stopReason);
    }

    static JSONObject parseArgs(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            return new JSONObject();
        }
        try {
            return new JSONObject(arguments);
        } catch (RuntimeException e) {
            Log.warning("openai tool_call arguments not valid JSON, defaulting to empty: " + arguments);
            return new JSONObject();
        }
    }

    static String extractErrorMessage(String body) {
        try {
            var json = new JSONObject(body);
            var error = json.optJSONObject("error");
            if (error != null) {
                var message = error.optString("message", null);
                if (message != null && !message.isBlank()) {
                    return message;
                }
            }
        } catch (RuntimeException ignored) {
        }
        return body;
    }

    static void populateUsage(OpenAIAPICallEvent event, JSONObject openaiResponse) {
        var usage = openaiResponse.optJSONObject("usage");
        if (usage == null) return;
        event.inputTokens = usage.optInt("prompt_tokens");
        event.outputTokens = usage.optInt("completion_tokens");
    }

    static void logTokens(OpenAIAPICallEvent event) {
        if (event.statusCode != 200) return;
        var max = maxTokens();
        var headroom = max - event.outputTokens;
        Log.tokens("in=%d out=%d/%d (headroom=%d)".formatted(
                event.inputTokens, event.outputTokens, max, headroom));
        if (event.outputTokens >= max * 0.9) {
            Log.warning("output tokens (%d) at %.0f%% of max (%d) — response may be truncated"
                    .formatted(event.outputTokens, 100.0 * event.outputTokens / max, max));
        }
    }

    static HttpResponse<String> send(String payload) {
        Log.agent("using openai model: %s".formatted(model()));
        var builder = HttpRequest.newBuilder(endpoint())
                .timeout(HttpTimeouts.requestTimeout())
                .POST(BodyPublishers.ofString(payload))
                .header("content-type", "application/json");
        var key = apiKey();
        if (key != null && !key.isBlank()) {
            builder.header("Authorization", "Bearer " + key);
        }
        try {
            return HttpTimeouts.client().send(builder.build(), BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Log.error(e.getMessage());
            throw new IllegalStateException("cannot communicate with openai endpoint", e);
        }
    }
}
