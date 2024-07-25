package airhacks.bedrock.control;

import java.util.List;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.IFunction;

public interface BedrockModelAccess {
    
    public static void grantInvoke(IFunction function){
        var policy =  PolicyStatement.Builder.create()
                .sid("BedrockInvokeAccess")
                .resources(List.of("arn:aws:bedrock:*::foundation-model/*"))
                .actions(List.of("bedrock:InvokeModel"))
                .effect(Effect.ALLOW)
                .build();   
        function.addToRolePolicy(policy);
    }
}
