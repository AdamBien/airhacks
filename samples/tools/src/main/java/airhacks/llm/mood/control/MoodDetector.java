package airhacks.llm.mood.control;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.bedrock.BedrockAnthropicMessageChatModel;
import dev.langchain4j.service.AiServices;

public class MoodDetector {

   // @Tool("calculates the mood. Pass the question to the parameter")
    public String howAreYou(String question) {
        System.out.println("-----------------");
        System.out.println(question);
        System.out.println("-----------------");
        return "I wrote some lean Java code with djl today!";
    }

    @Tool("identifies the country for which you have to find the capital")
    public String selectCountryForCapital() {
        return "norway";
    }

    public static AIGreeter create(BedrockAnthropicMessageChatModel model) {
        return AiServices
                .builder(AIGreeter.class)
                .chatLanguageModel(model)
                .tools(new MoodDetector())
                .build();
    }
}
