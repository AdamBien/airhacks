package airhacks.apidynamodbintegration.dynamodb.boundary;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.constructs.Construct;

public interface DynamoDBTables {
    
    static Table createRegistrationsTable(Construct scope) {
        
        return Table.Builder.create(scope, "RegistrationsTable")
                .tableName("registrations")
                .partitionKey(Attribute.builder()
                        .name("id")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
        .build();
    }
}