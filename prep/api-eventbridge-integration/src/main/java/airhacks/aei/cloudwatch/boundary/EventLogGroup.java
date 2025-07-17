package airhacks.aei.cloudwatch.boundary;

import software.amazon.awscdk.services.logs.*;
import software.constructs.Construct;

public interface EventLogGroup {
    
    static LogGroup createLogGroup(Construct scope) {
        return LogGroup.Builder.create(scope, "EventBridgeLogGroup")
                .logGroupName("/aws/events/airhacks-api-gateway-events")
                .retention(RetentionDays.ONE_WEEK)
                .build();
    }
}