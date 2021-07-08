package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Token;
import software.amazon.awscdk.services.sns.Subscription;
import software.amazon.awscdk.services.sns.SubscriptionProtocol;
import software.amazon.awscdk.services.sns.Topic;



public class CDKStack extends Stack {

    public CDKStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CDKStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        CfnParameter endpointName = CfnParameter.Builder.create(this, "httpEndpoint").type("String").description("The topic listener").build();
        var topic = Topic.Builder.create(this, "greetings-topic").topicName("greetings").fifo(false).build();
        Subscription.Builder.create(this, "greetings-subscription").protocol(SubscriptionProtocol.HTTP).endpoint(
                endpointName.getValueAsString()).topic(topic).build();
        CfnOutput.Builder.create(this, "sns.greetings.topic").value(topic.getTopicArn()).build();
    }



}
