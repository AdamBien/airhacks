package airhacks.aei.eventbridge.boundary;

import software.amazon.awscdk.services.events.*;
import software.amazon.awscdk.services.events.targets.CloudWatchLogGroup;
import software.amazon.awscdk.services.logs.ILogGroup;
import software.constructs.Construct;
import java.util.List;

public interface ApiEventBus {
    
    static EventBus createEventBus(Construct scope) {
        return EventBus.Builder.create(scope, "ApiEventBus")
                .eventBusName("airhacks-api-gateway-events")
                .build();
    }
    
    static void addLogGroupTarget(Construct scope, EventBus eventBus, ILogGroup logGroup) {
        var eventRule = Rule.Builder.create(scope, "ApiEventRule")
                .ruleName("airhacks-api-to-cloudwatch")
                .description("Sends all events from API Gateway to CloudWatch")
                .eventBus(eventBus)
                .eventPattern(EventPattern.builder()
                    .source(List.of("api.gateway"))
                    .build())
                .build();
        
        eventRule.addTarget(new CloudWatchLogGroup(logGroup));
    }
}