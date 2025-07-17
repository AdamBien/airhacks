package airhacks.workflowtrigger.stepfunctions.control;

import java.util.Map;

import software.amazon.awscdk.services.stepfunctions.IChainable;
import software.amazon.awscdk.services.stepfunctions.Pass;
import software.constructs.Construct;

public interface WorkflowStateMachineChain {

    static IChainable create(Construct scope) {
        var transformState = Pass.Builder.create(scope, "TransformInput")
                .comment("Transform input using JSONata")
                .assign(Map.of(
                    "original", "{% $states.input.body %}",
                    "requestId", "{% $uuid() %}",
                    "timestamp", "{% $now() %}"
                ))
                .build();
                
        var formatOutputState = Pass.Builder.create(scope, "FormatOutput")
                .comment("Format output with JSONata")
                .assign(Map.of(
                    "result", """
                        {% {
                            'status': 'completed',
                            'data': $original,
                            'requestId': $requestId,
                            'processedAt': $timestamp
                        } %}"""
                ))
                .build();
                
        return transformState.next(formatOutputState);
    }
}