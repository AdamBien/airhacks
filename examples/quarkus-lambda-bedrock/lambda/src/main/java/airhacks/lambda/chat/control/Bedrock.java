package airhacks.lambda.chat.control;

import java.io.StringReader;
import java.lang.System.Logger.Level;
import java.time.Duration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.lambda.chat.boundary.Chat;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.ForbiddenException;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.BedrockRuntimeException;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;

@ApplicationScoped
public class Bedrock {

    static System.Logger LOG = System.getLogger(Chat.class.getName());

    @Inject
    @ConfigProperty(name = "bedrock.version", defaultValue = "bedrock-2023-05-31")
    String bedrockVersion;


    ClaudeModel model = ClaudeModel.CLAUDE_3_SONNET;

    BedrockRuntimeClient runtimeClient;

    @PostConstruct
    public void initModel() {
        this.runtimeClient = BedrockRuntimeClient
                .builder()
                .httpClientBuilder(UrlConnectionHttpClient
                        .builder()
                        .socketTimeout(Duration.ofMinutes(5))
                        .connectionTimeout(Duration.ofMinutes(5)))
                .overrideConfiguration(ClientOverrideConfiguration
                        .builder()
                        .apiCallTimeout(Duration.ofMinutes(5))
                        .build())
                .build();
    }

    /**
     * https://docs.anthropic.com/claude/reference/complete_post
     * 
     * @param user
     * @return
     */
    public JsonObject invokeClaude(String system, String user, float temperature, int maxTokens) {

        var enclosedPrompt = Claude.messagePrompt(user);
        LOG.log(Level.DEBUG, enclosedPrompt.toString());
        var payloadJSON = Claude.claudeMessage(this.bedrockVersion, maxTokens, temperature, system, enclosedPrompt);

        var payload = payloadJSON.toString();
        LOG.log(Level.INFO, payload);

        LOG.log(Level.INFO, "AI is thinking...");
        var request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(model.modelId())
                .overrideConfiguration(AwsRequestOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofMinutes(5))
                        .build())
                .contentType("application/json")
                .accept("application/json")
                .build();
        try {
            var response = this.runtimeClient.invokeModel(request);
            var message = response.body().asUtf8String();
            LOG.log(Level.DEBUG, message);
            return fromString(message);
        } catch (BedrockRuntimeException ex) {
            var statusCode = ex.statusCode();
            var message = ex.getMessage();
            var errorMessage = switch (statusCode) {
                case 403 -> "Invalid credentials? status %d message: %s".formatted(statusCode, message);
                default -> "BedrockRuntimeException: status %d message: %s".formatted(statusCode, message);
            };

            if (statusCode == 403) {
                LOG.log(Level.ERROR, errorMessage);
                throw new ForbiddenException(errorMessage);
            }
            throw ex;
        }
    }

    public static JsonObject fromString(String stringified) {
        try (var reader = new StringReader(stringified);
                var jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }

}