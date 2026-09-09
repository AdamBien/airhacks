package airhacks.petstore.catalog.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;

/**
 * System tests for the catalog BC. Each row label is a requirement id
 * from catalog/package-info.java — the spec↔test trace.
 */
@QuarkusTest
public class CatalogIT {

    @Inject
    @RestClient
    CatalogResourceClient catalog;

    @Test
    public void r1_1_allCategoriesListed() {
        try (var response = this.catalog.categories()) {
            assertEquals(200, response.getStatus());
            var categories = response.readEntity(JsonObject[].class);
            assertTrue(categories.length > 0, "R1.1: categories expected");
        }
    }

    static Stream<Arguments> productsOfCategory() {
        return Stream.of(
                Arguments.of("R2.1", "FISH", 200),
                Arguments.of("R2.2", "NO-SUCH-CATEGORY", 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("productsOfCategory")
    public void listProducts(String requirement, String categoryId, int expectedStatus) {
        try (var response = this.catalog.products(categoryId)) {
            assertEquals(expectedStatus, response.getStatus(), requirement);
            if (expectedStatus == 200) {
                assertPageWithContent(response, requirement);
            }
        }
    }

    static Stream<Arguments> itemsOfProduct() {
        return Stream.of(
                Arguments.of("R3.1", "FI-SW-01", 200),
                Arguments.of("R3.2", "NO-SUCH-PRODUCT", 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("itemsOfProduct")
    public void listItems(String requirement, String productId, int expectedStatus) {
        try (var response = this.catalog.items(productId)) {
            assertEquals(expectedStatus, response.getStatus(), requirement);
            if (expectedStatus == 200) {
                assertPageWithContent(response, requirement);
            }
        }
    }

    static Stream<Arguments> itemById() {
        return Stream.of(
                Arguments.of("R4.1", "EST-1", 200),
                Arguments.of("R4.2", "NO-SUCH-ITEM", 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("itemById")
    public void getItem(String requirement, String itemId, int expectedStatus) {
        try (var response = this.catalog.item(itemId)) {
            assertEquals(expectedStatus, response.getStatus(), requirement);
            if (expectedStatus == 200) {
                var item = response.readEntity(JsonObject.class);
                assertEquals(itemId, item.getString("id"), requirement);
                assertFalse(item.getString("productId").isEmpty(), requirement + ": product reference expected");
                assertTrue(item.getJsonNumber("listPrice").doubleValue() > 0, requirement + ": list price expected");
            }
        }
    }

    static Stream<Arguments> searchProducts() {
        return Stream.of(
                Arguments.of("R5.1", "angel", true),
                Arguments.of("R5.2", "warp-core", false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("searchProducts")
    public void search(String requirement, String keyword, boolean matchesExpected) {
        try (var response = this.catalog.search(keyword)) {
            assertEquals(200, response.getStatus(), requirement);
            var page = response.readEntity(JsonObject.class);
            var content = page.getJsonArray("content");
            var total = page.getJsonNumber("total").longValue();
            if (matchesExpected) {
                assertFalse(content.isEmpty(), requirement + ": matches expected");
                assertTrue(total > 0, requirement);
            } else {
                assertTrue(content.isEmpty(), requirement + ": empty page expected");
                assertEquals(0, total, requirement);
            }
        }
    }

    static void assertPageWithContent(Response response, String requirement) {
        var page = response.readEntity(JsonObject.class);
        var content = page.getJsonArray("content");
        var total = page.getJsonNumber("total").longValue();
        assertFalse(content.isEmpty(), requirement + ": page content expected");
        assertTrue(total >= content.size(), requirement + ": total count expected");
    }
}
