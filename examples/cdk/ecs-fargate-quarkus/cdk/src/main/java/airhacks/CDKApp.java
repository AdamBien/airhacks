package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {
            var app = new App();
            Tags.of(app).add("project", "airhacks.live");
            Tags.of(app).add("environment","workshops");
            Tags.of(app).add("application", "ecs-fargate-quarkus");

        var environment = Environment.builder()
                        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                        .region(System.getenv("CDK_DEFAULT_REGION"))
                .build();
        
        var id = "ecs-fargate-quarkus";
        
        var stacksProps = StackProps.builder().env(environment).build();
        var ecrStack = new ECRStack(app, id,stacksProps);
        var repository = ecrStack.getRepository();
        new ECSFargateStack(app, id , stacksProps,repository);
        app.synth();
    }
}
