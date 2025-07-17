package airhacks.apidynamodbintegration.apigateway.boundary;

import java.util.List;

import software.amazon.awscdk.services.apigateway.CorsOptions;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.constructs.Construct;

public interface RestApis {
    
    static RestApi createApi(Construct scope) {
        var corsOptions = CorsOptions.builder()
                .allowOrigins(List.of("*"))
                .allowMethods(List.of("GET", "POST", "PUT", "DELETE"))
                .allowHeaders(List.of("*"))
                .build();
        
        return RestApi.Builder.create(scope, "RegistrationsApi")
                .restApiName("registrations-api")
                .defaultCorsPreflightOptions(corsOptions)
                .build();
    }
}