package airhacks.functionurl.boundary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class ServerlessApp {
    String region = "eu-central-1";
    App app;
    Optional<String> accountId;
    String stackId;
    String functionName;
    String functionHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";;
    Map<String, String> configuration = Map.of();
    String functionZipLocation;
    final int ONE_CPU = 1700;
    int ram = ONE_CPU;

    public ServerlessApp(App construct, String stackNamePrefix) {
        this.app = construct;
        this.stackId = stackNamePrefix.toLowerCase() + "-function-url";
        this.accountId = Optional.empty();
    }

    public ServerlessApp(String stackNamePrefix) {
        this(new App(), stackNamePrefix);
    }

    public ServerlessApp functionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public ServerlessApp functionHandler(String handler) {
        this.functionHandler = handler;
        return this;
    }

    public ServerlessApp accountId(String accountId) {
        this.accountId = Optional.of(accountId);
        return this;
    }

    public ServerlessApp ram(int ram) {
        this.ram = ram;
        return this;
    }

    public ServerlessApp withOneCPU() {
        this.ram = ONE_CPU;
        return this;
    }

    public ServerlessApp withHalfCPU() {
        this.ram = ONE_CPU / 2;
        return this;
    }

    public ServerlessApp withTwoCPUs() {
        this.ram = ONE_CPU * 2;
        return this;
    }

    /**
     * 
     * @param location the full path to the function.zip archive.
     * @return
     */
    public ServerlessApp functionZip(String location) {
        verifyFunctionZip(location);
        this.functionZipLocation = location;
        return this;
    }

    public ServerlessApp quarkusLambdaProjectLocation(String location) {
        this.functionZipLocation = location + "/target/function.zip";
        return this;
    }

    public ServerlessApp configuration(Map<String, String> configuration) {
        this.configuration = configuration;
        return this;
    }

    public FunctionURLStack build() {
        Objects.requireNonNull(this.functionName, "Function name is required");
        var stack = new FunctionURLStack(this);
        this.app.synth();
        return stack;
    }

    public StackProps stackProps() {
        var env = Environment.builder()
                .region(region);
        accountId.ifPresent(env::region);
        return StackProps.builder()
                .env(env.build())
                .build();
    }

    static boolean verifyFunctionZip(String functionZipFile) {
        if (!functionZipFile.endsWith("function.zip")) {
            throw new IllegalArgumentException("File must end with function.zip, but was: " + functionZipFile);
        }
        var exists = Files.exists(Path.of(functionZipFile));
        if (!exists) {
            throw new IllegalArgumentException("function.zip not found at: " + functionZipFile);
        }
        return true;
    }

}
