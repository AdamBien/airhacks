package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

import java.util.Arrays;

public class CDKApp {
    public static void main(final String[] args) {
            var app = new App();
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", "iam-user");

        var environment = Environment.builder()
                        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                        .region(System.getenv("CDK_DEFAULT_REGION"))
                        .build();
        var stacksProps = StackProps.builder().env(environment).build();
        new CDKStack(app, "iam-user", stacksProps);
        app.synth();
    }
}
