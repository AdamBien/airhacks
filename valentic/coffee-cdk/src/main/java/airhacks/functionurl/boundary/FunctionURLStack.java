package airhacks.functionurl.boundary;

import airhacks.lambda.control.FunctionURL;
import airhacks.lambda.control.QuarkusLambda;
import airhacks.s3.control.Persistence;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Tags;

public class FunctionURLStack extends Stack {


    public FunctionURLStack(ServerlessApp builder) {
        super(builder.app, builder.stackId,builder.stackProps());
        var quarkusLambda = new QuarkusLambda(this, builder.functionZipLocation, builder.functionName,
                builder.functionHandler, builder.ram,
                builder.configuration);
        var bucket = Persistence.create(this);
        var bucketName = bucket.getBucketName();
        quarkusLambda.addToConfiguration("BUCKET_NAME", bucketName);
        var function = quarkusLambda.create();
        bucket.grantReadWrite(function);
        var functionURL = FunctionURL.expose(function);
        addTags(builder.stackId);
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionURL.getUrl()).build();
    }

    void addTags(String appName) {
        Tags.of(this).add("application", appName);
        Tags.of(this).add("project", "AWS Lambda with FunctionURL");
        Tags.of(this).add("environment", "development");

    }
}
