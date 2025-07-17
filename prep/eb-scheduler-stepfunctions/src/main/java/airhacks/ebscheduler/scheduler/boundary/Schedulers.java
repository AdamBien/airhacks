package airhacks.ebscheduler.scheduler.boundary;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.events.*;
import software.amazon.awscdk.services.events.targets.SfnStateMachine;
import software.amazon.awscdk.services.stepfunctions.StateMachine;
import software.constructs.Construct;


import java.util.Map;

public interface Schedulers {
    Duration duration = Duration.minutes(1);
    
    static Rule create(Construct scope, String id, StateMachine stateMachine) {
        var rule = Rule.Builder.create(scope, id)
            .ruleName("WorkflowScheduleRule")
            .description("Triggers the Step Functions workflow every %s minutes".formatted(duration.toMinutes()))
            .schedule(Schedule.rate(duration))
            .build();
        
        var target = SfnStateMachine.Builder.create(stateMachine)
            .input(RuleTargetInput.fromObject(Map.of(
                "source", "EventBridge Rule",
                "createdTime", EventField.fromPath("$.time")
            )))
            .build();
        
        rule.addTarget(target);
        
        return rule;
    }
}