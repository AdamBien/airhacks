package airhacks.apidynamodbintegration.integration.boundary;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.services.apigateway.AwsIntegration;
import software.amazon.awscdk.services.apigateway.IntegrationOptions;
import software.amazon.awscdk.services.apigateway.IntegrationResponse;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

public interface DynamoDBIntegrations {
    
    static void setupDynamoDBIntegrations(Construct scope, RestApi api, Table table, Role integrationRole) {
        var registrations = api.getRoot().addResource("registrations");
        var registration = registrations.addResource("{id}");
        
        createPutItemIntegration(registrations, table, integrationRole);
        createDeleteItemIntegration(registration, table, integrationRole);
    }
    
   
    
    static void createPutItemIntegration(software.amazon.awscdk.services.apigateway.Resource resource, Table table, Role integrationRole) {
        var putItemIntegration = AwsIntegration.Builder.create()
                .service("dynamodb")
                .action("PutItem")
                .options(IntegrationOptions.builder()
                        .credentialsRole(integrationRole)
                        .requestTemplates(Map.of("application/json", """
                            #set($inputRoot = $input.path('$'))
                            {
                                "TableName": "%s",
                                "Item": {
                                    "id": {
                                        "S": "$context.requestId"
                                    },
                                    "content": {
                                        "S": "$util.escapeJavaScript($inputRoot)"
                                    },
                                    "timestamp": {
                                        "N": "$context.requestTimeEpoch"
                                    }
                                }
                            }
                            """.formatted(table.getTableName())))
                        .integrationResponses(List.of(
                                IntegrationResponse.builder()
                                        .statusCode("200")
                                        .responseTemplates(Map.of("application/json", """
                                            {
                                                "id": "$context.requestId",
                                                "message": "Registration created successfully"
                                            }
                                            """))
                                        .build()
                        ))
                        .build())
                .build();
        
        resource.addMethod("POST", putItemIntegration, MethodOptions.builder()
                .methodResponses(List.of(
                        MethodResponse.builder()
                                .statusCode("200")
                                .build()
                ))
                .build());
    }
    
  
  
    static void createDeleteItemIntegration(software.amazon.awscdk.services.apigateway.Resource resource, Table table, Role integrationRole) {
        var deleteItemIntegration = AwsIntegration.Builder.create()
                .service("dynamodb")
                .action("DeleteItem")
                .options(IntegrationOptions.builder()
                        .credentialsRole(integrationRole)
                        .requestTemplates(Map.of("application/json", """
                            {
                                "TableName": "%s",
                                "Key": {
                                    "id": {
                                        "S": "$input.params('id')"
                                    }
                                }
                            }
                            """.formatted(table.getTableName())))
                        .integrationResponses(List.of(
                                IntegrationResponse.builder()
                                        .statusCode("200")
                                        .responseTemplates(Map.of("application/json", """
                                            {
                                                "message": "Registration deleted successfully"
                                            }
                                            """))
                                        .build()
                        ))
                        .build())
                .build();
        
        resource.addMethod("DELETE", deleteItemIntegration, MethodOptions.builder()
                .methodResponses(List.of(
                        MethodResponse.builder()
                                .statusCode("200")
                                .build()
                ))
                .build());
    }
}