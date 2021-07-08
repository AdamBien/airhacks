package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ssm.StringParameter;

public class CDKStack extends Stack {
    public CDKStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CDKStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var parameter = StringParameter.Builder.create(this, "airhacks-hello-parameters").parameterName("message")
                .stringValue("hello,world").build();
        CfnOutput.Builder.create(this, "airhacks-hello-parameter-output").value(parameter.getParameterArn()).build();
    }
}
