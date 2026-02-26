package airhacks.qmp.health.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class HealthCheckIT {

    @Inject
    @RestClient
    HealthClient client;

    @Test
    void livenessReturnsUp() {
        try (var response = this.client.liveness()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    void readinessReturnsUp() {
        try (var response = this.client.readiness()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }
}
