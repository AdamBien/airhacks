package airhacks.nova.bedrock.control;

import java.time.Duration;

import org.json.JSONObject;

import airhacks.nova.amazon.control.Nova;
import airhacks.nova.logging.control.Log;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelSummary;
import software.amazon.awssdk.services.bedrock.model.InferenceType;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsRequest;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;

public interface Bedrock {
        Region REGION = Region.US_EAST_1;
        String BEDROCK_VERSION = "bedrock-2023-05-31";

        /**
         * https://docs.anthropic.com/claude/docs/models-overview#model-comparison
         * https://docs.anthropic.com/claude/reference/messages_post
         */

        enum Model {
                NOVA_PRO("amazon.nova-pro-v1:0", Region.US_EAST_1),
                NOVA_MICRO("amazon.nova-micro-v1:0", Region.US_EAST_1);

                String modelId;
                Region region;

                Model(String modelId, Region region) {
                        this.modelId = modelId;
                        this.region = region;
                }

                public String modelId() {
                        return this.modelId;
                }

                public Region region() {
                        return this.region;
                }
        }

        Model nova = Model.NOVA_MICRO;


        static String getPrompt(String system, String user) {
                if (system == null || system.isEmpty()) {
                        return "\n\nHuman: %s\n\nAssistant:".formatted(user);
                } else {
                        return """
                                        %s
                                        \n\nHuman: %s\n\nAssistant:
                                        """.formatted(system, user);
                }
        }



        static String summary(FoundationModelSummary modelSummary) {
                return "%s - %s - customization: %b".formatted(modelSummary.modelId(), modelSummary.modelName(),
                                modelSummary.customizationsSupported());
        }




        static JSONObject invokeNova(String user, int maxTokens,float temperature) {
                var modelId = nova.modelId();
                var client = bedrockRuntimeClient();
                var jsonRequest = Nova.payload(user,maxTokens, temperature,0.9f);
                var requestMessage = jsonRequest.toString();
                Log.debug(requestMessage);

                Log.SYSTEM.out("%s is thinking...".formatted(modelId));
                var request = InvokeModelRequest.builder()
                                .body(SdkBytes.fromUtf8String(requestMessage))
                                .modelId(modelId)
                                .overrideConfiguration(AwsRequestOverrideConfiguration.builder()
                                                .apiCallTimeout(Duration.ofMinutes(5))
                                                .build())
                                .contentType("application/json")
                                .accept("application/json")
                                .build();
                var response = client.invokeModel(request);
                var message = response.body().asUtf8String();
                Log.debug(message);
                return new JSONObject(message);
        
        }



        private static BedrockRuntimeClient bedrockRuntimeClient() {
                var client = BedrockRuntimeClient
                                .builder()
                                .region(nova.region())
                                .httpClientBuilder(UrlConnectionHttpClient
                                                .builder()
                                                .socketTimeout(Duration.ofMinutes(5))
                                                .connectionTimeout(Duration.ofMinutes(5)))
                                .overrideConfiguration(ClientOverrideConfiguration
                                                .builder()
                                                .apiCallTimeout(Duration.ofMinutes(5))
                                                .build())
                                .build();
                return client;
        }

        private static BedrockClient bedrockClient() {
                return BedrockClient.builder()
                                .region(nova.region())
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

        public static void listModels() {
                var request = ListFoundationModelsRequest
                                .builder()
                                .byInferenceType(InferenceType.ON_DEMAND)
                                .build();
                var response = bedrockClient()
                                .listFoundationModels(request);
                response
                                .modelSummaries()
                                .stream()
                                .map(Bedrock::summary)
                                .forEach(Log.INFO::out);

        }        

}
