package airhacks.lambda.chat.control;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;


public interface Claude {
    

    static JsonObject claudeMessage(String bedrockVersion,int maxTokens,float temperature, String system, JsonArray messages) {
        return Json.createObjectBuilder()
                .add("anthropic_version", bedrockVersion)
                .add("max_tokens", maxTokens)
                .add("messages", messages)
                .add("temperature", temperature)
                .add("system", system)
                .build();

    }

    static JsonArray messagePrompt(String user) {
        var message = message("user", user);
        return Json.createArrayBuilder()
                .add(message)
                .build();

    }

    static JsonObject message(String role, String content) {
        return Json.createObjectBuilder()
                .add("role", role)
                .add("content", content)
                .build();
    }

}
