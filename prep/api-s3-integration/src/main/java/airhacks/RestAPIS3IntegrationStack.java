package airhacks;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import airhacks.registrations.storage.boundary.Storage;
import airhacks.registrations.apigateway.boundary.RegistrationsApi;

public class RestAPIS3IntegrationStack extends Stack {

    public RestAPIS3IntegrationStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
        
        var bucket = Storage.createRegistrationsBucket(this);
        var api = RegistrationsApi.createApi(this);
        
        RegistrationsApi.addS3Integration(api, bucket);
    }
}
