package airhacks.lambda.chat.control;

import software.amazon.awssdk.regions.Region;

public enum ClaudeModel {

        /**
         * Claude 3 Sonnet by Anthropic strikes the ideal balance between intelligence
         * and speed—particularly for enterprise workloads. It offers maximum utility at
         * a lower price than competitors, and is engineered to be the dependable,
         * high-endurance workhorse for scaled AI deployments. Claude 3 Sonnet can
         * process images and return text outputs, and features a 200K context window.
         */
        CLAUDE_3_SONNET("anthropic.claude-3-sonnet-20240229-v1:0"),
        /**
         * Customer interactions: quick and accurate support in live interactions,
         * translations
         * Content moderation: catch risky behavior or customer requests
         * Cost-saving tasks: optimized logistics, inventory management, extract
         * knowledge from unstructured data
         */
        CLAUDE_3_HAIKU("anthropic.claude-3-haiku-20240307-v1:0");

        String modelId;
        Region region;

        ClaudeModel(String modelId) {
                this.modelId = modelId;
        }

        public String modelId() {
                return this.modelId;
        }

}