package airhacks;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.assertions.Template;
import software.amazon.awscdk.assertions.Match;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.List;

class SchedulerStepFunctionsStackTest {
    
    @Test
    void shouldCreateStepFunctionsStateMachine() {
        var app = new App();
        var stack = new SchedulerStepFunctionsStack(app, "TestStack", StackProps.builder().build());
        var template = Template.fromStack(stack);
        
        template.hasResourceProperties("AWS::StepFunctions::StateMachine", Map.of(
            "StateMachineName", "ScheduledWorkflowStateMachine",
            "DefinitionString", Match.anyValue()
        ));
    }
    
    @Test
    void shouldCreateEventBridgeRule() {
        var app = new App();
        var stack = new SchedulerStepFunctionsStack(app, "TestStack", StackProps.builder().build());
        var template = Template.fromStack(stack);
        
        template.hasResourceProperties("AWS::Events::Rule", Map.of(
            "Name", "WorkflowScheduleRule",
            "ScheduleExpression", "rate(5 minutes)"
        ));
    }
    
    @Test
    void shouldCreateEventBridgeRuleTarget() {
        var app = new App();
        var stack = new SchedulerStepFunctionsStack(app, "TestStack", StackProps.builder().build());
        var template = Template.fromStack(stack);
        
        template.hasResourceProperties("AWS::Events::Rule", Map.of(
            "Targets", Match.arrayWith(List.of(
                Match.objectLike(Map.of(
                    "Arn", Match.anyValue(),
                    "RoleArn", Match.anyValue()
                ))
            ))
        ));
    }
    
    @Test
    void shouldCreateLambdaFunction() {
        var app = new App();
        var stack = new SchedulerStepFunctionsStack(app, "TestStack", StackProps.builder().build());
        var template = Template.fromStack(stack);
        
        template.hasResourceProperties("AWS::Lambda::Function", Map.of(
            "Runtime", "nodejs20.x",
            "MemorySize", 512,
            "Timeout", 30
        ));
    }
}