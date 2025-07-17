package airhacks.workflowtrigger.cloudwatchlogs.control;

import software.amazon.awscdk.services.stepfunctions.LogOptions;
import software.amazon.awscdk.services.stepfunctions.LogLevel;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

public interface StepFunctionsLogging {

    static LogOptions create(Construct scope) {
        var logGroup = LogGroup.Builder.create(scope, "WorkflowLogGroup")
                .logGroupName("/aws/stepfunctions/api-stepfunctions-integration")
                .retention(RetentionDays.ONE_WEEK)
                .build();

        return LogOptions.builder()
                .destination(logGroup)
                .level(LogLevel.ALL)
                .includeExecutionData(true)
                .build();
    }
}