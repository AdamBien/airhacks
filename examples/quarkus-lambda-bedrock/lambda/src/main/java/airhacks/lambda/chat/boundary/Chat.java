package airhacks.lambda.chat.boundary;

import java.lang.System.Logger.Level;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.lambda.chat.control.Bedrock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@ApplicationScoped
public class Chat {

    final static String UNSET = "-not set-";

    static System.Logger LOG = System.getLogger(Chat.class.getName());

    @Inject
    Bedrock bedrock;


    @Inject
    @ConfigProperty(defaultValue = """
            You are my assistant, you are answer must be correct and fact-based
            """, name = "system.prompt")
    String systemPrompt;    

    @Inject
    @ConfigProperty(name = "max.tokens", defaultValue = "4000")
    int maxTokens;

    @Inject
    @ConfigProperty(name = "temperature", defaultValue = "0.0f")
    float temperature;


    public String ask(String userRequest) {
        LOG.log(Level.INFO, "user request (length: %d) received: ".formatted(userRequest.length()));
        var claudeResponse = this.bedrock.invokeClaude(systemPrompt, userRequest, temperature, maxTokens);
        System.out.println(claudeResponse);
        return extractAnswer(claudeResponse);
    }

    /**
     * Extracts the answer as text from claude json
     */
    static String extractAnswer(JsonObject input) {
        var contentArray = input.getJsonArray("content");
        var contentObjects = contentArray.getValuesAs(JsonObject.class);
        return contentObjects
                .stream()
                .map(json -> json.getString("text"))
                .collect(Collectors.joining("\n\n"));
    }

}
