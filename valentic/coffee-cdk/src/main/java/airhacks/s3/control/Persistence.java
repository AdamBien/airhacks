package airhacks.s3.control;

import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.constructs.Construct;

public interface Persistence {
    
    static IBucket create(Construct scope){
        var bucket = Bucket.Builder.create(scope, "CoffeeBucket")
        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
        .build();
        Tags.of(bucket).add("name", "CoffeBucket");
        return bucket;
    }
}
