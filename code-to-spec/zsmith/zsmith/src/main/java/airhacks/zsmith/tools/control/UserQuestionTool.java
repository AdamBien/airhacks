package airhacks.zsmith.tools.control;

import java.util.function.Function;

import org.json.JSONObject;

public interface UserQuestionTool {

    enum Field { question }

    static ToolHandler create() {
        return create(Console::prompt);
    }

    static ToolHandler create(Function<String, String> promptFunction) {
        return ToolHandler.of(
                "user_question",
                "Asks the user a question and returns the typed answer",
                ToolHandler.schema(ToolHandler.Prop.string(Field.question, "The question to ask the user")),
                input -> run(input, promptFunction));
    }

    private static String run(JSONObject input, Function<String, String> promptFunction) {
        if (!input.has(Field.question.name()) || input.getString(Field.question.name()).isEmpty()) {
            throw new IllegalArgumentException("Missing or empty required parameter: question");
        }
        var question = input.getString(Field.question.name());
        var prompt = question + ": ";
        return promptFunction.apply(prompt);
    }
}
