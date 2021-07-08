package airhacks;

import org.jetbrains.annotations.Nullable;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecr.Repository;
import software.constructs.Construct;

public class ECRStack extends Stack {
    
    static final String ID = "ecr";

    Repository repository;

    public ECRStack(@Nullable Construct scope,String repositoryName,StackProps stackProps) {
        super(scope, ID,stackProps);
        this.repository = Repository
                .Builder.create(this, ID)
                        .repositoryName("airhacks/" + repositoryName).removalPolicy(RemovalPolicy.DESTROY)
        .build();
    }

    public Repository getRepository() {
        return repository;
    }
    
    
}
