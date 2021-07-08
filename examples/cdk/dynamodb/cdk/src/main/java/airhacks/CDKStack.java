package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;


public class CDKStack extends Stack {
    public CDKStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CDKStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        
        var table = Table.Builder.create(this, "workshops")
        .billingMode(BillingMode.PAY_PER_REQUEST)
        .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
        .sortKey(Attribute.builder().name("name").type(AttributeType.STRING).build())
        .tableName("workshops").build();
        
        CfnOutput.Builder.create(this, "workshops-table-output").value(table.getTableName()).build();
                
    }
}
