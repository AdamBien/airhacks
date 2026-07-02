package airhacks.zsmith.agent.entity;

import airhacks.zsmith.configuration.control.ZCfg;

public record AgentDefaults(String name, String systemPrompt, int maxIterations, float temperature) {

    public static final String NAME = "zsmith";
    public static final String SYSTEM_PROMPT = "You are a helpful assistant.";
    public static final int MAX_ITERATIONS = 100;
    public static final float TEMPERATURE = 0.1f;

    public static AgentDefaults fromConfig() {
        return new AgentDefaults(
                ZCfg.string("agent.name", NAME),
                ZCfg.string("agent.system.prompt", SYSTEM_PROMPT),
                ZCfg.integer("agent.max.iterations", MAX_ITERATIONS),
                parseFloat(ZCfg.string("agent.temperature", null), TEMPERATURE));
    }

    static float parseFloat(String value, float fallback) {
        if (value == null || value.isBlank()) return fallback;
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
