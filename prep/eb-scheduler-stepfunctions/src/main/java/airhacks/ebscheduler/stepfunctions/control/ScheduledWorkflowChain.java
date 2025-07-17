package airhacks.ebscheduler.stepfunctions.control;

import software.amazon.awscdk.services.stepfunctions.*;
import software.constructs.Construct;
import java.util.Map;

public interface ScheduledWorkflowChain {

    static IChainable create(Construct scope) {
        var captureEventState = new Pass(scope, "TransformInput",
                PassProps.builder()
                        .comment("Store the origin event details")
                        .assign(Map.of(
                                "source", "{% $states.input.source %}",
                                "processedAt", "{% $now() %}",
                                "scheduledAt","{% $states.input.createdTime %}"
                                ))
                        .build());

        var formatOutputState = Pass.Builder.create(scope, "FormatOutput")
                .comment("Workflow completed successfully")
                .assign(Map.of(
                        "result", """
                                {% {
                                    'status': 'completed',
                                    'source': $source,
                                    'processedAt': $processedAt,
                                    'scheduledAt': $scheduledAt
                                } %}"""))
                .build();

        return captureEventState.next(formatOutputState);
    }
}