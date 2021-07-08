package airhacks;

import software.constructs.Construct;

import java.util.Arrays;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cognito.AccountRecovery;
import software.amazon.awscdk.services.cognito.AutoVerifiedAttrs;
import software.amazon.awscdk.services.cognito.CfnIdentityPool;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClient;
import software.amazon.awscdk.services.pinpointemail.CfnIdentityProps;

public class CDKStack extends Stack {
    public CDKStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CDKStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var userPool = UserPool.Builder.create(this, "airhacks-pool").autoVerify(AutoVerifiedAttrs.builder().email(true).build())
                .accountRecovery(AccountRecovery.EMAIL_AND_PHONE_WITHOUT_MFA).build();
        var userPoolClient = UserPoolClient.Builder.create(this, "airhacks-pool-client").userPool(userPool)
                .generateSecret(false).build();
                
        CfnIdentityPool.Builder.create(this, "airhacks-identity-pool")
        .allowUnauthenticatedIdentities(false)
                .cognitoIdentityProviders(userPool.getIdentityProviders())
                .build();
        
        CfnOutput.Builder.create(this, "userpool.id").value(userPool.getUserPoolId()).build();
        CfnOutput.Builder.create(this, "userpool.client.id").value(userPoolClient.getUserPoolClientId()).build();

        
    }
}
