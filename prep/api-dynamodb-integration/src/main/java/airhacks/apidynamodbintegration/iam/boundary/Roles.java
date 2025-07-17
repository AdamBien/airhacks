package airhacks.apidynamodbintegration.iam.boundary;

import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public interface Roles {
    
    static Role createApiGatewayDynamoDBRole(Construct scope, Table table) {
        var role = Role.Builder.create(scope, "ApiGatewayDynamoDBRole")
                .roleName("RestAPIDynamoDBAccess")
                .assumedBy(new ServicePrincipal("apigateway.amazonaws.com"))
                .description("Role for API Gateway to access DynamoDB")
                .build();
        
        table.grantReadWriteData(role);
        
        return role;
    }
}