package airhacks;

import software.constructs.Construct;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.CfnAccessKey;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.Group;
import software.amazon.awscdk.services.iam.IManagedPolicy;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Policy;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.User;

public class CDKStack extends Stack {

    static final List<String> policies = Arrays.asList(new String[]{ "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryFullAccess",
            "arn:aws:iam::aws:policy/AmazonS3FullAccess", "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess",
            "arn:aws:iam::aws:policy/AmazonECS_FullAccess", "arn:aws:iam::aws:policy/AmazonRoute53AutoNamingFullAccess",
            "arn:aws:iam::aws:policy/AmazonRoute53FullAccess", "arn:aws:iam::aws:policy/IAMFullAccess",
            "arn:aws:iam::aws:policy/AWSCodeCommitPowerUser" });

    public CDKStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CDKStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        var managedPolicies = policies.stream().map(this::toManagedPolicy).toList();
        var group = Group.Builder.create(this, name("group")).groupName(name("group")).managedPolicies(managedPolicies).build();

        var user = User.Builder.create(this, name("user")).groups(Arrays.asList(group))
                .userName(name("user")).build();
        group.addUser(user);
        group.addToPolicy(PolicyStatement.Builder.create()
                .resources(Arrays.asList("*"))
        .effect(Effect.ALLOW)
                .actions(Arrays.asList("*"))
                .build());
        System.out.println("------------------- " + user.getUserArn());
        var accessKey = CfnAccessKey.Builder.create(this, name("access-key")).userName(user.getUserName()).build();
        CfnOutput.Builder.create(this, name("arn-output")).value(user.getUserArn()).build();
        CfnOutput.Builder.create(this, name("access-key-id-output")).value(accessKey.getRef()).build();
        CfnOutput.Builder.create(this, name("access-secret-key-output")).value(accessKey.getAttrSecretAccessKey()).build();
        
            
    }
    
    static String name(String type) {
        return "airhacks-admin-" + type;
    }


    IManagedPolicy toManagedPolicy(String policy) {
        var id = extractPolicyName(policy);
        return ManagedPolicy.fromManagedPolicyArn(this, id, policy);
    }

    static String extractPolicyName(String nameWithArn) {
        var lastIndex  = nameWithArn.lastIndexOf("/");
        return nameWithArn.substring(lastIndex+1);
    }
}
