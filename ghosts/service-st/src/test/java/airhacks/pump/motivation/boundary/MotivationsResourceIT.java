package airhacks.pump.motivation.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class MotivationsResourceIT {

    @Inject
    @RestClient
    MotivationsResourceClient rut;

    @Test
    void arnoldSpeaks() {
        var encouragement = this.rut.encouragement();
        assertThat(encouragement.getString("wisdom")).isNotBlank();
        assertThat(encouragement.getString("saying")).isNotBlank();
    }
}
