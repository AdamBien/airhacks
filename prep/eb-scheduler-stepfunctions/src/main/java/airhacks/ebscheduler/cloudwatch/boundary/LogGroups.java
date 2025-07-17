package airhacks.ebscheduler.cloudwatch.boundary;

import software.amazon.awscdk.services.logs.*;
import software.constructs.Construct;

public interface LogGroups {
    
    static LogGroup createForStateMachine(Construct scope, String stateMachineName) {
        return LogGroup.Builder.create(scope, "StateMachineLogGroup")
            .logGroupName("/aws/stepfunctions/" + stateMachineName)
            .retention(RetentionDays.ONE_WEEK)
            .build();
    }
}