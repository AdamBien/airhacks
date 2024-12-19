package airhacks.llm.claude.control;

import dev.langchain4j.model.bedrock.BedrockAnthropicMessageChatModel;
import software.amazon.awssdk.regions.Region;

public interface ClaudeAccess {

    public enum BedrockModels {
        CLAUDE_SONNET_3("anthropic.claude-3-sonnet-20240229-v1:0"),
        CLAUDE_SONNET_3_5("anthropic.claude-3-5-sonnet-20240620-v1:0");

        private final String modelId;

        BedrockModels(String modelId) {
            this.modelId = modelId;
        }

        public String modelID() {
            return this.modelId;
        }

    }

    static BedrockAnthropicMessageChatModel model() {
        var modelId = BedrockModels.CLAUDE_SONNET_3_5
                .modelID();
        return BedrockAnthropicMessageChatModel
                .builder()
                .temperature(0)
                .maxTokens(1000)
                .region(Region.EU_CENTRAL_1)
                .model(modelId)
                .maxRetries(2)
                .build();
    }


}
