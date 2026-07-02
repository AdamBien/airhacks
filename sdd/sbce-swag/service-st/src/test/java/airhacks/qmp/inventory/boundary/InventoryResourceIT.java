package airhacks.qmp.inventory.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class InventoryResourceIT {

    @Inject
    @RestClient
    InventoryResourceClient rut;

    @Test
    public void setStock() { // R1.1
        try (var response = this.rut.setStock(new StockRequest("T_SHIRT", "L", 25))) {
            assertThat(response.getStatus()).isEqualTo(204);
        }
    }

    @Test
    public void listAvailability() { // R3.1
        try (var response = this.rut.availability()) {
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }
}
