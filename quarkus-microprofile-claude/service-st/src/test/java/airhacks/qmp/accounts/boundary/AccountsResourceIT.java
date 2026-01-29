package airhacks.qmp.accounts.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class AccountsResourceIT {

    @Inject
    @RestClient
    AccountsResourceClient client;

    @Test
    void accounts() {
        var response = this.client.accounts();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void create() {
        var payload = """
                {
                    "accountNumber": "039158547000",
                    "routingCode": "SBIN0001234",
                    "balance": 1000.00,
                    "owner": {
                        "name": "Duke",
                        "email": "duke@java.net",
                        "phone": "+91-9876543210",
                        "address": {
                            "street": "MG Road 42",
                            "city": "Bangalore",
                            "postalCode": "560001",
                            "country": "IN"
                        }
                    },
                    "currency": "INR"
                }
                """;
        var response = this.client.create(payload);
        assertThat(response.getStatus()).isEqualTo(201);
    }
}
