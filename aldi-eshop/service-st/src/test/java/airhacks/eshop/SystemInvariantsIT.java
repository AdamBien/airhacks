package airhacks.eshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airhacks.eshop.catalog.boundary.ProductsResourceClient;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;

@QuarkusTest
public class SystemInvariantsIT {

    @Inject
    @RestClient
    ProductsResourceClient products;

    @Test
    @DisplayName("S1 — When a catalog read is served, the system shall include caching metadata in the response.")
    public void s1CatalogReadIncludesCachingMetadata() {
        try (var response = this.products.listProductsRaw()) {
            assertThat(response.getHeaderString(HttpHeaders.CACHE_CONTROL)).isNotBlank();
        }
    }

    @Test
    @DisplayName("S2 — When a catalog read is served, the system shall respond within 200 milliseconds.")
    public void s2CatalogReadRespondsWithin200Milliseconds() {
        this.products.listProducts(null);
        var start = System.nanoTime();
        this.products.listProducts(null);
        var elapsed = Duration.ofNanos(System.nanoTime() - start);
        assertThat(elapsed.toMillis()).isLessThan(200);
    }
}
