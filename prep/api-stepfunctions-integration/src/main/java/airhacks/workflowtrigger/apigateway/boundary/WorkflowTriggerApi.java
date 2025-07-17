package airhacks.workflowtrigger.apigateway.boundary;

import java.util.List;

import airhacks.workflowtrigger.cloudwatchlogs.control.ApiGatewayLogging;
import software.amazon.awscdk.services.apigateway.AccessLogFormat;
import software.amazon.awscdk.services.apigateway.LogGroupLogDestination;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.apigateway.StepFunctionsExecutionIntegrationOptions;
import software.amazon.awscdk.services.apigateway.StepFunctionsIntegration;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.stepfunctions.IStateMachine;
import software.constructs.Construct;

public interface WorkflowTriggerApi {
        String apiName = "DeveloperWorkflowTriggerApi";

    static RestApi create(Construct scope, IStateMachine stateMachine, IRole integrationRole) {
        var logGroup = ApiGatewayLogging.createAccessLogGroup(scope);
        
        var restApi = RestApi.Builder.create(scope, "WorkflowTriggerRestApi")
                .restApiName(apiName)
                .description("REST API to trigger Step Functions workflow")
                .cloudWatchRole(true)
                .deployOptions(StageOptions.builder()
                        .accessLogDestination(new LogGroupLogDestination(logGroup))
                        .accessLogFormat(AccessLogFormat.jsonWithStandardFields())
                        .build())
                .build();
                
        var integration = StepFunctionsIntegration.startExecution(stateMachine,
                StepFunctionsExecutionIntegrationOptions.builder()
                        .credentialsRole(integrationRole)
                        .build()
        );
        
        var workflowResource = restApi.getRoot().addResource("workflow");
        workflowResource.addMethod("POST", integration,
                MethodOptions.builder()
                        .methodResponses(List.of(
                                MethodResponse.builder()
                                        .statusCode("200")
                                        .build()
                        ))
                        .build()
        );
        
        return restApi;
    }
}