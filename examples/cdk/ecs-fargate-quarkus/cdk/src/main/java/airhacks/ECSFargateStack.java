package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;

public class ECSFargateStack extends Stack {
 
    public ECSFargateStack(final Construct scope, final String appName, StackProps props, Repository repository) {
        super(scope, appName,props);
        var vpc = Vpc.Builder.create(this, appName+"-vpc")
                .maxAzs(3) // Default is all AZs in region
                .build();

        var image = ContainerImage.fromEcrRepository(repository);

        var cluster = Cluster.Builder.create(this, appName+"-cluster").vpc(vpc).build();
        var service = ApplicationLoadBalancedFargateService.Builder.create(this, appName + "-service")
        .cluster(cluster) // Required
                .cpu(512) // Default is 256
                .desiredCount(1) // Default is 1
                .taskImageOptions(
                    ApplicationLoadBalancedTaskImageOptions
                .builder()
                                .image(image)
                                .containerPort(8080)
                                .containerName(appName)
                                .enableLogging(true)
                        .build()
                        )
                .memoryLimitMiB(1024) // Default is 512
                .publicLoadBalancer(true) // Default is false
                .build();
        var clusterName = cluster.getClusterName();
        var serviceName = service.getService().getServiceName();
        CfnOutput.Builder.create(this, "ecs.cluster.name").value(clusterName).build();
        CfnOutput.Builder.create(this, "ecs.service.name").value(serviceName).build();
        CfnOutput.Builder.create(this, "ecs.deployment.cmd")
                .value(String.format("aws ecs update-service --force-new-deployment --cluster %s  --service %s --profile cli",
                        clusterName, serviceName))
                .build();
        
    }
}
