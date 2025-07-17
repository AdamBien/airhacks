package airhacks.workflowtrigger.iam.control;

import software.constructs.Construct;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.stepfunctions.IStateMachine;

public interface ServiceIntegrationRoles {

    static Role createApiGatewayToStepFunctionsRole(Construct scope, IStateMachine stateMachine) {
        var role = Role.Builder.create(scope, "ApiGatewayStepFunctionsIntegrationRole")
                .roleName("ApiGatewayStepFunctionsExecutor")
                .assumedBy(new ServicePrincipal("apigateway.amazonaws.com"))
                .build();
                
        stateMachine.grantStartSyncExecution(role);
        stateMachine.grantStartExecution(role);
        
        return role;
    }
}