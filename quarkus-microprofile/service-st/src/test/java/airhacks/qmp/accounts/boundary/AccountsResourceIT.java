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
                    "iban": "DE89370400440532013000",
                    "balance": 1000.00,
                    "owner": "Duke",
                    "currency": "EUR"
                }
                """;
        var response = this.client.create(payload);
        assertThat(response.getStatus()).isEqualTo(201);
    }
}
