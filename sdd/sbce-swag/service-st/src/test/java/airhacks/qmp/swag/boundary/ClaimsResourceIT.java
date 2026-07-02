package airhacks.qmp.swag.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import airhacks.qmp.inventory.boundary.InventoryResourceClient;
import airhacks.qmp.inventory.boundary.StockRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ClaimsResourceIT {

    @Inject
    @RestClient
    ClaimsResourceClient rut;

    @Inject
    @RestClient
    InventoryResourceClient inventory;

    void stock(String tShirtSize, String socksSize) {
        this.inventory.setStock(new StockRequest("T_SHIRT", tShirtSize, 100)).close();
        this.inventory.setStock(new StockRequest("SOCKS", socksSize, 100)).close();
    }

    static Claim claimFor(String email) {
        var address = new ShippingAddress("Duke", "Java", "1 Java Street", "12345", "Palo Alto", "USA");
        return new Claim(email, "L", "M", address);
    }

    @Test
    public void claimSwag() { // R1.1
        stock("L", "M");
        var claim = claimFor("duke-" + UUID.randomUUID() + "@airhacks.live");
        try (var response = this.rut.claim(claim)) {
            assertThat(response.getStatus()).isEqualTo(201);
        }
    }

    @Test
    public void oneClaimPerAttendee() { // R2.1
        stock("L", "M");
        var claim = claimFor("dup-" + UUID.randomUUID() + "@airhacks.live");
        try (var first = this.rut.claim(claim)) {
            assertThat(first.getStatus()).isEqualTo(201);
        }
        try (var second = this.rut.claim(claim)) {
            assertThat(second.getStatus()).isEqualTo(409);
        }
    }
}
