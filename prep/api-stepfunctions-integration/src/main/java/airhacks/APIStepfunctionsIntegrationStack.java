package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.CfnOutput;
import airhacks.workflowtrigger.stepfunctions.control.WorkflowStateMachines;
import airhacks.workflowtrigger.apigateway.boundary.WorkflowTriggerApi;
import airhacks.workflowtrigger.iam.control.ServiceIntegrationRoles;

public class APIStepfunctionsIntegrationStack extends Stack {

    public APIStepfunctionsIntegrationStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
        
        var stateMachine = WorkflowStateMachines.create(this);
        var integrationRole = ServiceIntegrationRoles.createApiGatewayToStepFunctionsRole(this, stateMachine);
        var restApi = WorkflowTriggerApi.create(this, stateMachine, integrationRole);
        
        CfnOutput.Builder.create(this, "WorkflowTriggerApiEndpoint")
                .value(restApi.getUrl() + "workflow")
                .description("REST API endpoint to trigger developer data transformation workflow")
                .build();
    }
}
