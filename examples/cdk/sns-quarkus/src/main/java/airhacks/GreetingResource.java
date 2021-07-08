package airhacks;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Path("/greetings")
public class GreetingResource {

    @Inject
    SnsClient sns;

    @ConfigProperty(name="sns.greetings.topic") 
    String snsTopic;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String hello(String message) {
        var request = PublishRequest.builder()
                .message(message)
                .topicArn(snsTopic)
        .build();
        var response = sns.publish(request);
        return response.messageId();
    }
}