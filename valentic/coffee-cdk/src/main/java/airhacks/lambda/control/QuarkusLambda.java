package airhacks.lambda.control;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public final class QuarkusLambda extends Construct {

    static int timeout = 10;
    static Map<String,String> RUNTIME_CONFIGURATION = Map.of(
            "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1");

    Map<String, String> configuration;
    String functionZip;
    String functionName;
    String lambdaHandler;
    int ramInMb;

    public QuarkusLambda(Construct scope, String functionZip,String functionName,String lambdaHandler, int ramInMb,Map<String,String> applicationConfiguration) {
        super(scope, "QuarkusLambda");
        this.functionZip = functionZip;
        this.functionName = functionName;
        this.lambdaHandler = lambdaHandler;
        this.ramInMb = ramInMb;
        this.configuration = mergeWithRuntimeConfiguration(applicationConfiguration);
    }

    public void addToConfiguration(String key,String value){
        this.configuration.put(key, value);
    }


    IFunction createFunction(String functionZip,String functionName, String functionHandler, Map<String, String> configuration, int memory,
            int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_21)
                .architecture(Architecture.ARM_64)
                .code(Code.fromAsset(functionZip))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .build();
    }

    static Map<String,String> mergeWithRuntimeConfiguration(Map<String,String> applicationConfiguuration){
        var configuration = new HashMap<>(RUNTIME_CONFIGURATION);
        configuration.putAll(applicationConfiguuration);
        return configuration;
    }

    public IFunction create() {
        return createFunction(this.functionZip,this.functionName, this.lambdaHandler, this.configuration, this.ramInMb, timeout);
    }
}