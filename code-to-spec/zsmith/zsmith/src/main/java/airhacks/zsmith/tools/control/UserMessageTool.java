package airhacks.zsmith.tools.control;

import java.util.function.Consumer;

import org.json.JSONObject;

import airhacks.zsmith.logging.control.Log;

public interface UserMessageTool {

    enum Field { message }

    static ToolHandler create() {
        return create(Log::user);
    }

    static ToolHandler create(Consumer<String> messageConsumer) {
        return ToolHandler.of(
                "user_message",
                "Presents a message to the user. Use this to display important information, status updates, or notifications.",
                ToolHandler.schema(ToolHandler.Prop.string(Field.message, "The message to present to the user")),
                input -> run(input, messageConsumer));
    }

    private static String run(JSONObject input, Consumer<String> messageConsumer) {
        if (!input.has(Field.message.name()) || input.getString(Field.message.name()).isEmpty()) {
            return "Error: Missing or empty required parameter: message";
        }
        var message = input.getString(Field.message.name());
        messageConsumer.accept(message);
        return "Message presented to user";
    }
}
