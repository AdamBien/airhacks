package airhacks.ebank.leasing.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class LeasesResourceIT {

    @Inject
    @RestClient
    LeasesResourceClient client;

    @Test
    @DisplayName("creates lease and retrieves it by id")
    void createAndRetrieve() {
        var leaseJSON = """
                {
                  "contractNumber": "LEASE-2025-001",
                  "lesseeIban": "DE89370400440532013000",
                  "amount": 50000,
                  "durationMonths": 36,
                  "startDate": "2025-01-15"
                }
                """;

        var createResponse = this.client.create(leaseJSON);
        assertThat(createResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());

        var createdLease = createResponse.readEntity(JsonObject.class);
        var leaseId = createdLease.getJsonNumber("id").longValue();
        assertThat(createdLease.getString("contractNumber")).isEqualTo("LEASE-2025-001");
        assertThat(createdLease.getString("lesseeIban")).isEqualTo("DE89370400440532013000");
        assertThat(createdLease.getInt("amount")).isEqualTo(50000);
        assertThat(createdLease.getInt("durationMonths")).isEqualTo(36);

        var retrieveResponse = this.client.lease(leaseId);
        assertThat(retrieveResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        var retrievedLease = retrieveResponse.readEntity(JsonObject.class);
        assertThat(retrievedLease.getString("contractNumber")).isEqualTo("LEASE-2025-001");
        assertThat(retrievedLease.getString("endDate")).isEqualTo("2028-01-15");
    }

    @Test
    @DisplayName("retrieves non-existent lease returns no content")
    void nonExistentLease() {
        var response = this.client.lease(999999L);
        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }
}
