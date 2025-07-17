package airhacks.workflowtrigger.stepfunctions.control;

import software.amazon.awscdk.services.stepfunctions.DefinitionBody;
import software.amazon.awscdk.services.stepfunctions.QueryLanguage;
import software.amazon.awscdk.services.stepfunctions.StateMachine;
import software.amazon.awscdk.services.stepfunctions.StateMachineType;
import software.constructs.Construct;
import airhacks.workflowtrigger.cloudwatchlogs.control.StepFunctionsLogging;

public interface WorkflowStateMachines {
    String stateMachineName = "DeveloperDataTransformationWorkflow";

    static StateMachine create(Construct scope) {
        var definition = WorkflowStateMachineChain.create(scope);
        var logging = StepFunctionsLogging.create(scope);

        return StateMachine.Builder.create(scope, "DeveloperProcessingWorkflow")
                .stateMachineName(stateMachineName)
                .queryLanguage(QueryLanguage.JSONATA)
                .stateMachineType(StateMachineType.EXPRESS)
                .definitionBody(DefinitionBody.fromChainable(definition))
                .logs(logging)
                .build();
    }
}