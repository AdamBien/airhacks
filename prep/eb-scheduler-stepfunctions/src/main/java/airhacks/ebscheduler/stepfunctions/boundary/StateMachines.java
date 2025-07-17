package airhacks.ebscheduler.stepfunctions.boundary;

import airhacks.ebscheduler.cloudwatch.boundary.LogGroups;
import airhacks.ebscheduler.stepfunctions.control.ScheduledWorkflowChain;
import software.amazon.awscdk.services.stepfunctions.*;
import software.amazon.awscdk.Duration;
import software.constructs.Construct;

public interface StateMachines {
    
    static StateMachine create(Construct scope, String id) {
        var chain = ScheduledWorkflowChain.create(scope);
        var stateMachineName = "ScheduledWorkflowStateMachine";
        var logGroup = LogGroups.createForStateMachine(scope, stateMachineName);
        
        return StateMachine.Builder.create(scope, id)
            .stateMachineType(StateMachineType.EXPRESS)
            .queryLanguage(QueryLanguage.JSONATA)
            .stateMachineName(stateMachineName)
            .definitionBody(DefinitionBody.fromChainable(chain))
            .timeout(Duration.minutes(5))
            .comment("Scheduled workflow that processes data periodically")
            .logs(LogOptions.builder()
                .destination(logGroup)
                .level(LogLevel.ALL)
                .includeExecutionData(true)
                .build())
            .build();
    }
}