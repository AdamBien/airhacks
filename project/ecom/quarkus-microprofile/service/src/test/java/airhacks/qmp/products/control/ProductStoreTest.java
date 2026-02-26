package airhacks.qmp.products.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import airhacks.qmp.products.entity.Color;
import airhacks.qmp.products.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductStoreTest {

    ProductStore store;

    @BeforeEach
    void setUp() {
        store = new ProductStore();
    }

    @Test
    void addThenAllReturnsProduct() {
        var product = new Product("p1", "Widget", "A useful widget", new BigDecimal("29.99"), Color.BLUE);
        store.add(product);

        var all = store.all();

        assertEquals(1, all.size());
        assertEquals(product, all.get(0));
    }

    @Test
    void findByIdReturnsCorrectProduct() {
        var product = new Product("p2", "Gadget", "A handy gadget", new BigDecimal("9.95"), Color.GREEN);
        store.add(product);

        var found = store.findById("p2");

        assertTrue(found.isPresent());
        assertEquals(product, found.get());
    }

    @Test
    void findByIdReturnsEmptyForNonExistentId() {
        var result = store.findById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void allReturnsEmptyListWhenNoProductsAdded() {
        var all = store.all();

        assertTrue(all.isEmpty());
    }
}
