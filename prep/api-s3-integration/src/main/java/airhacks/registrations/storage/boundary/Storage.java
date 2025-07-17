package airhacks.registrations.storage.boundary;

import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public interface Storage {
    
    static Bucket createRegistrationsBucket(Construct scope) {
        var stack = Stack.of(scope);
        return Bucket.Builder.create(scope, "RegistrationsBucket")
                .bucketName("%s-registrations".formatted(stack.getAccount()))
                .build();
    }
}