package airhacks;

import airhacks.ebscheduler.stepfunctions.boundary.StateMachines;
import airhacks.ebscheduler.scheduler.boundary.Schedulers;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class SchedulerStepFunctionsStack extends Stack {

    public SchedulerStepFunctionsStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
        
        var stateMachine = StateMachines.create(this, "ScheduledWorkflow");
        Schedulers.create(this, "WorkflowScheduler", stateMachine);
    }
}
