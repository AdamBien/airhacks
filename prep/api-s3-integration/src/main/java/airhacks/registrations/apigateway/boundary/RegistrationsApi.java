package airhacks.registrations.apigateway.boundary;

import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.AwsIntegration;
import software.amazon.awscdk.services.apigateway.IntegrationOptions;
import software.amazon.awscdk.services.apigateway.IntegrationResponse;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.PassthroughBehavior;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;
import java.util.List;
import java.util.Map;

public interface RegistrationsApi {
    
    static RestApi createApi(Construct scope) {
        return RestApi.Builder.create(scope, "RegistrationsApi")
                .restApiName("registrations-api")
                .description("API for managing registrations in S3")
                .build();
    }
    
    static void addS3Integration(RestApi api, Bucket bucket) {
        var registrationsResource = api.getRoot().addResource("registrations");
        var registrationResource = registrationsResource.addResource("{id}");
        
        var apiRole = Role.Builder.create(api, "ApiGatewayS3Role")
                .assumedBy(new ServicePrincipal("apigateway.amazonaws.com"))
                .build();
        
        bucket.grantReadWrite(apiRole);
        
        addPutMethod(registrationResource, bucket.getBucketName(), apiRole);
        addGetMethod(registrationResource, bucket.getBucketName(), apiRole);
    }
    
    private static void addPutMethod(software.amazon.awscdk.services.apigateway.Resource resource, String bucketName, Role apiRole) {
        var integrationOptions = IntegrationOptions.builder()
                .credentialsRole(apiRole)
                .passthroughBehavior(PassthroughBehavior.WHEN_NO_MATCH)
                .requestParameters(Map.of(
                        "integration.request.path.bucket", "'" + bucketName + "'",
                        "integration.request.path.key", "method.request.path.id"
                ))
                .integrationResponses(List.of(
                        IntegrationResponse.builder()
                                .statusCode("200")
                                .responseParameters(Map.of(
                                        "method.response.header.Content-Type", "integration.response.header.Content-Type"
                                ))
                                .build()
                ))
                .build();
        
        var integration = AwsIntegration.Builder.create()
                .service("s3")
                .integrationHttpMethod("PUT")
                .path("{bucket}/{key}")
                .options(integrationOptions)
                .build();
        
        resource.addMethod("PUT", integration, MethodOptions.builder()
                .requestParameters(Map.of("method.request.path.id", true))
                .methodResponses(List.of(
                        MethodResponse.builder()
                                .statusCode("200")
                                .responseParameters(Map.of(
                                        "method.response.header.Content-Type", true
                                ))
                                .build()
                ))
                .build());
    }
    
    private static void addGetMethod(software.amazon.awscdk.services.apigateway.Resource resource, String bucketName, Role apiRole) {
        var integrationOptions = IntegrationOptions.builder()
                .credentialsRole(apiRole)
                .passthroughBehavior(PassthroughBehavior.WHEN_NO_MATCH)
                .requestParameters(Map.of(
                        "integration.request.path.bucket", "'" + bucketName + "'",
                        "integration.request.path.key", "method.request.path.id"
                ))
                .integrationResponses(List.of(
                        IntegrationResponse.builder()
                                .statusCode("200")
                                .responseParameters(Map.of(
                                        "method.response.header.Content-Type", "integration.response.header.Content-Type"
                                ))
                                .build()
                ))
                .build();
        
        var integration = AwsIntegration.Builder.create()
                .service("s3")
                .integrationHttpMethod("GET")
                .path("{bucket}/{key}")
                .options(integrationOptions)
                .build();
        
        resource.addMethod("GET", integration, MethodOptions.builder()
                .requestParameters(Map.of("method.request.path.id", true))
                .methodResponses(List.of(
                        MethodResponse.builder()
                                .statusCode("200")
                                .responseParameters(Map.of(
                                        "method.response.header.Content-Type", true
                                ))
                                .build()
                ))
                .build());
    }
}