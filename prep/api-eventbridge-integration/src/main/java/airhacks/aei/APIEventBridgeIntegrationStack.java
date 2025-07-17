package airhacks.aei;

import software.constructs.Construct;
import airhacks.aei.apigateway.boundary.EventsApi;
import airhacks.aei.cloudwatch.boundary.EventLogGroup;
import airhacks.aei.eventbridge.boundary.ApiEventBus;
import airhacks.aei.integration.control.EventBridgeIntegrationFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class APIEventBridgeIntegrationStack extends Stack {

    public APIEventBridgeIntegrationStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
        
        var eventBus = ApiEventBus.createEventBus(this);
        var logGroup = EventLogGroup.createLogGroup(this);
        var api = EventsApi.createApi(this);
        
        var integrationWithRole = EventBridgeIntegrationFactory.createIntegration(this, eventBus.getEventBusName());
        
        eventBus.grantPutEventsTo(integrationWithRole.role());
        ApiEventBus.addLogGroupTarget(this, eventBus, logGroup);
        
        EventsApi.addEventBridgeIntegration(api, integrationWithRole.integration());
    }
}
