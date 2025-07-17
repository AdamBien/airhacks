package airhacks.aei.apigateway.boundary;

import software.amazon.awscdk.services.apigateway.*;
import software.constructs.Construct;
import java.util.List;
import java.util.Map;

public interface EventsApi {
    
    static RestApi createApi(Construct scope) {
        return RestApi.Builder.create(scope, "EventBridgeApi")
                .restApiName("airhacks-eventbridge-api")
                .description("API Gateway directly integrated with EventBridge")
                .build();
    }
    
    static void addEventBridgeIntegration(RestApi api, AwsIntegration integration) {
        var eventsResource = api.getRoot().addResource("events");
        
        var methodResponses = List.of(
            MethodResponse.builder()
                .statusCode("200")
                .responseModels(Map.of("application/json", Model.EMPTY_MODEL))
                .build()
        );
        
        eventsResource.addMethod("PUT", integration,
            MethodOptions.builder()
                .methodResponses(methodResponses)
                .build()
        );
    }
}