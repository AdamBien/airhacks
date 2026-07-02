package airhacks.zsmith.llm.control;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zsmith.claude.control.Claude;
import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.lightmetal.control.LightMetal;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.openai.control.OpenAI;

public interface LLM {

    enum Provider {
        CLAUDE {
            @Override
            public JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
                return Claude.invoke(system, messages, tools, temperature);
            }
        },
        BEDROCK {
            @Override
            public JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
                return Claude.invoke(system, messages, tools, temperature);
            }
        },
        OPENAI {
            @Override
            public JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
                return OpenAI.invoke(system, messages, tools, temperature);
            }
        },
        LIGHTMETAL {
            @Override
            public JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
                return LightMetal.invoke(system, messages, tools, temperature);
            }
        };

        public abstract JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature);

        public static Provider fromConfig() {
            if (LightMetal.available()) {
                var configured = ZCfg.string("llm.provider", null);
                if (configured != null && !"lightmetal".equalsIgnoreCase(configured)) {
                    Log.agent("lightmetal.jar on classpath — overriding llm.provider=" + configured);
                }
                return LIGHTMETAL;
            }
            var configured = ZCfg.string("llm.provider", "claude");
            try {
                return Provider.valueOf(configured.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(
                        "unknown llm.provider '%s' — expected one of %s"
                                .formatted(configured, java.util.Arrays.toString(values())));
            }
        }
    }

    static JSONObject invoke(String system, JSONArray messages, JSONArray tools, float temperature) {
        return Provider.fromConfig().invoke(system, messages, tools, temperature);
    }
}
