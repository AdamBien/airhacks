package airhacks.eshop.catalog.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@QuarkusTest
public class ProductsResourceIT {

    @Inject
    @RestClient
    ProductsResourceClient rut;

    @Test
    public void listProducts() {
        var products = this.rut.listProducts(null);
        assertThat(products).isNotEmpty();
    }

    @Test
    public void listProductsByCategory() {
        var products = this.rut.listProducts("dairy");
        assertThat(products)
                .isNotEmpty()
                .allSatisfy(product -> assertThat(((JsonObject) product).getString("category")).isEqualTo("dairy"));
    }

    @Test
    public void findProduct() {
        var product = this.rut.findProduct("p-milk");
        assertThat(product.getString("id")).isEqualTo("p-milk");
    }
}
