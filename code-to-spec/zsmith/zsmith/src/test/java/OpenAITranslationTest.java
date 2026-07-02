import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.openai.control.OpenAI;

void main() {
    // OpenAI helpers read openai.model / openai.max.tokens from ZCfg
    ZCfg.loadBaseConfig("zsmith-test-openai-" + ProcessHandle.current().pid());

    textOnlyAssistant();
    toolUseOnlyAssistant();
    mixedTextAndToolUse();
    singleToolResultUserMessage();
    multipleToolResultsInOneMessage();
    isErrorPrefix();
    multiToolCallResponse();
    emptyArgumentsString();
    malformedArgumentsString();
    missingArgumentsField();
    errorMessageExtraction();
    errorMessageFallback();
    threeTurnRoundTrip();
    toolDefinitionTranslation();
    systemPromptPrepended();

    System.out.println("OpenAITranslationTest passed");
}

void textOnlyAssistant() {
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "assistant").put("content",
                    new JSONArray().put(new JSONObject().put("type", "text").put("text", "hello"))));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var out = payload.getJSONArray("messages");
    assert out.length() == 1 : "expected single message, got " + out.length();
    var assistant = out.getJSONObject(0);
    assert "assistant".equals(assistant.getString("role"));
    assert "hello".equals(assistant.getString("content"));
    assert !assistant.has("tool_calls") : "no tool_calls expected on text-only message";
}

void toolUseOnlyAssistant() {
    var blocks = new JSONArray()
            .put(new JSONObject().put("type", "tool_use")
                    .put("id", "toolu_1").put("name", "calc").put("input", new JSONObject().put("a", 2)));
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "assistant").put("content", blocks));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var assistant = payload.getJSONArray("messages").getJSONObject(0);
    assert JSONObject.NULL.equals(assistant.get("content")) : "content should be JSON null for tool-only message";
    var calls = assistant.getJSONArray("tool_calls");
    assert calls.length() == 1;
    var call = calls.getJSONObject(0);
    assert "toolu_1".equals(call.getString("id"));
    assert "function".equals(call.getString("type"));
    assert "calc".equals(call.getJSONObject("function").getString("name"));
    var args = new JSONObject(call.getJSONObject("function").getString("arguments"));
    assert args.getInt("a") == 2;
}

void mixedTextAndToolUse() {
    var blocks = new JSONArray()
            .put(new JSONObject().put("type", "text").put("text", "thinking..."))
            .put(new JSONObject().put("type", "tool_use")
                    .put("id", "t1").put("name", "calc").put("input", new JSONObject()));
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "assistant").put("content", blocks));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var assistant = payload.getJSONArray("messages").getJSONObject(0);
    assert "thinking...".equals(assistant.getString("content"));
    assert assistant.getJSONArray("tool_calls").length() == 1;
}

void singleToolResultUserMessage() {
    var blocks = new JSONArray()
            .put(new JSONObject().put("type", "tool_result")
                    .put("tool_use_id", "t1").put("content", "42"));
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "user").put("content", blocks));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var out = payload.getJSONArray("messages");
    assert out.length() == 1;
    var toolMsg = out.getJSONObject(0);
    assert "tool".equals(toolMsg.getString("role"));
    assert "t1".equals(toolMsg.getString("tool_call_id"));
    assert "42".equals(toolMsg.getString("content"));
}

void multipleToolResultsInOneMessage() {
    var blocks = new JSONArray()
            .put(new JSONObject().put("type", "tool_result").put("tool_use_id", "a").put("content", "1"))
            .put(new JSONObject().put("type", "tool_result").put("tool_use_id", "b").put("content", "2"));
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "user").put("content", blocks));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var out = payload.getJSONArray("messages");
    assert out.length() == 2 : "expected 2 tool messages, got " + out.length();
    assert "a".equals(out.getJSONObject(0).getString("tool_call_id"));
    assert "b".equals(out.getJSONObject(1).getString("tool_call_id"));
}

void isErrorPrefix() {
    var blocks = new JSONArray()
            .put(new JSONObject().put("type", "tool_result")
                    .put("tool_use_id", "t1").put("content", "division by zero")
                    .put("is_error", true));
    var messages = new JSONArray()
            .put(new JSONObject().put("role", "user").put("content", blocks));

    var payload = OpenAI.translateRequest(null, messages, null, 0.5f);
    var content = payload.getJSONArray("messages").getJSONObject(0).getString("content");
    assert "ERROR: division by zero".equals(content) : "expected ERROR prefix, got: " + content;
}

void multiToolCallResponse() {
    var openaiResponse = new JSONObject().put("choices", new JSONArray().put(new JSONObject()
            .put("message", new JSONObject()
                    .put("role", "assistant")
                    .put("content", JSONObject.NULL)
                    .put("tool_calls", new JSONArray()
                            .put(callOf("c1", "add", "{\"a\":1}"))
                            .put(callOf("c2", "sub", "{\"b\":2}"))))
            .put("finish_reason", "tool_calls")));

    var anthropic = OpenAI.translateResponse(openaiResponse);
    assert "tool_use".equals(anthropic.getString("stop_reason"));
    var content = anthropic.getJSONArray("content");
    assert content.length() == 2 : "expected 2 tool_use blocks";
    assert "c1".equals(content.getJSONObject(0).getString("id"));
    assert "add".equals(content.getJSONObject(0).getString("name"));
    assert content.getJSONObject(0).getJSONObject("input").getInt("a") == 1;
    assert "c2".equals(content.getJSONObject(1).getString("id"));
}

void emptyArgumentsString() {
    var input = OpenAI.parseArgs("");
    assert input.isEmpty() : "empty args should produce empty object";
}

void malformedArgumentsString() {
    var input = OpenAI.parseArgs("{not valid json");
    assert input.isEmpty() : "malformed args should fall back to empty object";
}

void missingArgumentsField() {
    var openaiResponse = new JSONObject().put("choices", new JSONArray().put(new JSONObject()
            .put("message", new JSONObject()
                    .put("role", "assistant")
                    .put("content", JSONObject.NULL)
                    .put("tool_calls", new JSONArray().put(new JSONObject()
                            .put("id", "c1")
                            .put("type", "function")
                            .put("function", new JSONObject().put("name", "noop")))))
            .put("finish_reason", "tool_calls")));

    var anthropic = OpenAI.translateResponse(openaiResponse);
    var block = anthropic.getJSONArray("content").getJSONObject(0);
    assert block.getJSONObject("input").isEmpty();
}

void errorMessageExtraction() {
    var body = new JSONObject().put("error",
            new JSONObject().put("message", "Invalid API key").put("type", "invalid_request_error")).toString();
    var message = OpenAI.extractErrorMessage(body);
    assert "Invalid API key".equals(message) : "expected extracted message, got: " + message;
}

void errorMessageFallback() {
    var body = "<html>503 service unavailable</html>";
    var message = OpenAI.extractErrorMessage(body);
    assert body.equals(message) : "non-JSON body should round-trip unchanged";
}

void threeTurnRoundTrip() {
    var memory = new JSONArray()
            .put(new JSONObject().put("role", "user").put("content", "what is 2+2?"))
            .put(new JSONObject().put("role", "assistant").put("content", new JSONArray()
                    .put(new JSONObject().put("type", "text").put("text", "let me calculate"))
                    .put(new JSONObject().put("type", "tool_use")
                            .put("id", "tu_1").put("name", "calc")
                            .put("input", new JSONObject().put("expr", "2+2")))))
            .put(new JSONObject().put("role", "user").put("content", new JSONArray()
                    .put(new JSONObject().put("type", "tool_result")
                            .put("tool_use_id", "tu_1").put("content", "4"))));

    var payload = OpenAI.translateRequest("you are a calculator", memory, null, 0.7f);
    var msgs = payload.getJSONArray("messages");
    assert msgs.length() == 4 : "expected system + user + assistant + tool = 4 messages, got " + msgs.length();
    assert "system".equals(msgs.getJSONObject(0).getString("role"));
    assert "you are a calculator".equals(msgs.getJSONObject(0).getString("content"));
    assert "user".equals(msgs.getJSONObject(1).getString("role"));
    assert "what is 2+2?".equals(msgs.getJSONObject(1).getString("content"));
    assert "assistant".equals(msgs.getJSONObject(2).getString("role"));
    assert "let me calculate".equals(msgs.getJSONObject(2).getString("content"));
    assert msgs.getJSONObject(2).getJSONArray("tool_calls").length() == 1;
    assert "tool".equals(msgs.getJSONObject(3).getString("role"));
    assert "tu_1".equals(msgs.getJSONObject(3).getString("tool_call_id"));
}

void toolDefinitionTranslation() {
    var tools = new JSONArray().put(new JSONObject()
            .put("name", "calculator")
            .put("description", "adds numbers")
            .put("input_schema", new JSONObject().put("type", "object")
                    .put("properties", new JSONObject().put("a", new JSONObject().put("type", "number")))));
    var messages = new JSONArray().put(new JSONObject().put("role", "user").put("content", "go"));

    var payload = OpenAI.translateRequest(null, messages, tools, 0.5f);
    var openaiTools = payload.getJSONArray("tools");
    assert openaiTools.length() == 1;
    var first = openaiTools.getJSONObject(0);
    assert "function".equals(first.getString("type"));
    var fn = first.getJSONObject("function");
    assert "calculator".equals(fn.getString("name"));
    assert "adds numbers".equals(fn.getString("description"));
    assert "object".equals(fn.getJSONObject("parameters").getString("type"));
}

void systemPromptPrepended() {
    var messages = new JSONArray().put(new JSONObject().put("role", "user").put("content", "hi"));
    var payload = OpenAI.translateRequest("be polite", messages, null, 0.5f);
    var msgs = payload.getJSONArray("messages");
    assert msgs.length() == 2;
    assert "system".equals(msgs.getJSONObject(0).getString("role"));
    assert "be polite".equals(msgs.getJSONObject(0).getString("content"));

    // null/blank system not prepended
    var payload2 = OpenAI.translateRequest(null, messages, null, 0.5f);
    assert payload2.getJSONArray("messages").length() == 1;
    var payload3 = OpenAI.translateRequest("   ", messages, null, 0.5f);
    assert payload3.getJSONArray("messages").length() == 1;
}

JSONObject callOf(String id, String name, String arguments) {
    return new JSONObject()
            .put("id", id)
            .put("type", "function")
            .put("function", new JSONObject().put("name", name).put("arguments", arguments));
}
