package airhacks.aei.integration.control;

import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.iam.*;
import software.constructs.Construct;
import java.util.List;
import java.util.Map;

public interface EventBridgeIntegrationFactory {
    record IntegrationWithRole(AwsIntegration integration, Role role) {}
    
    static IntegrationWithRole createIntegration(Construct scope, String eventBusName) {
        var apiGatewayRole = Role.Builder.create(scope, "ApiGatewayEventBridgeRole")
                .assumedBy(new ServicePrincipal("apigateway.amazonaws.com"))
                .build();
        
        // VTL mapping reference: https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-mapping-template-reference.html
        var integrationOptions = IntegrationOptions.builder()
                .credentialsRole(apiGatewayRole)
                .requestParameters(Map.of(
                    "integration.request.header.X-Amz-Target", "'AWSEvents.PutEvents'",
                    "integration.request.header.Content-Type", "'application/x-amz-json-1.1'"
                ))
                .requestTemplates(Map.of(
                    "application/json", """
                        {
                            "Entries": [{
                                "Source": "api.gateway",
                                "DetailType": "API Request",
                                "Detail": "$util.escapeJavaScript($input.json('$'))",
                                "EventBusName": "%s"
                            }]
                        }
                        """.formatted(eventBusName)
                ))
                .integrationResponses(List.of(
                    IntegrationResponse.builder()
                        .statusCode("200")
                        .responseTemplates(Map.of(
                            "application/json", """
                                {
                                    "message": "Event published successfully",
                                    "eventId": "$input.path('$.Entries[0].EventId')"
                                }
                                """
                        ))
                        .build()
                ))
                .passthroughBehavior(PassthroughBehavior.NEVER)
                .build();
        
        var integration = AwsIntegration.Builder.create()
                .service("events")
                .action("PutEvents")
                .integrationHttpMethod("POST")
                .options(integrationOptions)
                .build();
                
        return new IntegrationWithRole(integration, apiGatewayRole);
    }
    
    
}