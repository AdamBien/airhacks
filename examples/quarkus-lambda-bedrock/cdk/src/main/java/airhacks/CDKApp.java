package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        var appName = "quarkus-lambda-bedrock";

        Tags.of(app).add("project", "Bedrock / Anthropic Claude v3 Access");
        Tags.of(app).add("environment", "development");
        Tags.of(app).add("application", appName);

        var stack = new InfrastructureBuilder(app, appName)
                .functionName("airhacks_QuarkusBedrock")
                .functionURLBuilder()
                .build();        
        app.synth();
    }
}
