package airhacks.qmp.owners.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class OwnersResourceIT {

    @Inject
    @RestClient
    OwnersResourceClient client;

    @Test
    void owners() {
        var response = this.client.owners();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void create() {
        var payload = """
                {
                    "name": "Duke",
                    "email": "duke@java.net",
                    "phone": "+91-9876543210",
                    "address": {
                        "street": "MG Road 42",
                        "city": "Bangalore",
                        "postalCode": "560001",
                        "country": "IN"
                    }
                }
                """;
        var response = this.client.create(payload);
        assertThat(response.getStatus()).isEqualTo(201);
    }
}
