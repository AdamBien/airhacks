package valantic.persistence.control;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class Persistence {

    Logger LOG = System.getLogger("persistence");

    static S3Client client = S3Client.create();

    @Inject
    @ConfigProperty(name = "bucket.name")
    String bucketName;

    public void save(String key, String value) {
        LOG.log(Level.INFO, "writing key %s ");
        var request = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();
        client.putObject(request,RequestBody.fromString(value));
        LOG.log(Level.INFO, "saving " + key + " " + value);
    }

}
