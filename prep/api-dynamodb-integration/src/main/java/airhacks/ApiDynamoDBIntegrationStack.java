package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import airhacks.apidynamodbintegration.dynamodb.boundary.DynamoDBTables;
import airhacks.apidynamodbintegration.apigateway.boundary.RestApis;
import airhacks.apidynamodbintegration.iam.boundary.Roles;
import airhacks.apidynamodbintegration.integration.boundary.DynamoDBIntegrations;

public class ApiDynamoDBIntegrationStack extends Stack {

    public ApiDynamoDBIntegrationStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
        
        var registrationsTable = DynamoDBTables.createRegistrationsTable(this);
        var api = RestApis.createApi(this);
        var integrationRole = Roles.createApiGatewayDynamoDBRole(this, registrationsTable);
        
        DynamoDBIntegrations.setupDynamoDBIntegrations(this, api, registrationsTable, integrationRole);
    }
}
