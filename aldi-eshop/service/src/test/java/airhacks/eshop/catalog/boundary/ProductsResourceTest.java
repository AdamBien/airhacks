package airhacks.eshop.catalog.boundary;

import static airhacks.eshop.catalog.Requirement.Rn.R1_1;
import static airhacks.eshop.catalog.Requirement.Rn.R1_2;
import static airhacks.eshop.catalog.Requirement.Rn.R1_3;
import static airhacks.eshop.catalog.Requirement.Rn.R1_4;
import static airhacks.eshop.catalog.Requirement.Rn.R2_1;
import static airhacks.eshop.catalog.Requirement.Rn.R2_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.eshop.catalog.Requirement;
import airhacks.eshop.catalog.control.ProductStore;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.NotFoundException;

class ProductsResourceTest {

    ProductsResource catalog;

    @BeforeEach
    void init() {
        this.catalog = new ProductsResource();
        this.catalog.store = new ProductStore();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listProductsCases")
    void listProducts(Requirement.Rn requirement, String category, Predicate<JsonArray> expectation) {
        var response = this.catalog.listProducts(category);
        var products = (JsonArray) response.getEntity();
        assertTrue(expectation.test(products), requirement + " — " + requirement.statement());
    }

    static Stream<Arguments> listProductsCases() {
        Predicate<JsonArray> everyProduct = products -> products.size() == 4;
        Predicate<JsonArray> onlyDairy = products -> products.size() == 2
                && products.stream()
                        .map(JsonObject.class::cast)
                        .allMatch(product -> product.getString("category").equals("dairy"));
        Predicate<JsonArray> empty = JsonArray::isEmpty;
        Predicate<JsonArray> unavailableFlagged = products -> products.stream()
                .map(JsonObject.class::cast)
                .anyMatch(product -> !product.getBoolean("available"));
        return Stream.of(
                arguments(R1_1, null, everyProduct),
                arguments(R1_2, "dairy", onlyDairy),
                arguments(R1_3, "electronics", empty),
                arguments(R1_4, null, unavailableFlagged));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findProductCases")
    void findProduct(Requirement.Rn requirement, String id, boolean offered) {
        if (offered) {
            var response = this.catalog.findProduct(id);
            var product = (JsonObject) response.getEntity();
            assertEquals(id, product.getString("id"), requirement + " — " + requirement.statement());
        } else {
            assertThrows(NotFoundException.class, () -> this.catalog.findProduct(id),
                    requirement + " — " + requirement.statement());
        }
    }

    static Stream<Arguments> findProductCases() {
        return Stream.of(
                arguments(R2_1, "p-milk", true),
                arguments(R2_2, "p-unknown", false));
    }
}
