package airhacks.workflowtrigger.cloudwatchlogs.control;

import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.apigatewayv2.CfnStage;
import software.constructs.Construct;

public interface ApiGatewayLogging {

    static LogGroup createAccessLogGroup(Construct scope) {
        return LogGroup.Builder.create(scope, "ApiAccessLogGroup")
                .logGroupName("/aws/apigateway/api-stepfunctions-integration/access-logs")
                .retention(RetentionDays.ONE_WEEK)
                .build();
    }
    

}