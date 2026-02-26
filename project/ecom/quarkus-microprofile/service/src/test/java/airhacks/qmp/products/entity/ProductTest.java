package airhacks.qmp.products.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void toJSON() {
        var product = new Product("p1", "Widget", "A useful widget", new BigDecimal("29.99"), Color.BLUE);
        var json = product.toJSON();

        assertEquals("p1", json.getString("productId"));
        assertEquals("Widget", json.getString("name"));
        assertEquals("A useful widget", json.getString("description"));
        assertEquals(new BigDecimal("29.99"), json.getJsonNumber("price").bigDecimalValue());
        assertEquals("blue", json.getString("color"));
    }

    @Test
    void fromJSON() {
        var json = jakarta.json.Json.createObjectBuilder()
                .add("productId", "p1")
                .add("name", "Widget")
                .add("description", "A useful widget")
                .add("price", new BigDecimal("29.99"))
                .add("color", "blue")
                .build();

        var product = Product.fromJSON(json);

        assertEquals("p1", product.productId());
        assertEquals("Widget", product.name());
        assertEquals("A useful widget", product.description());
        assertEquals(new BigDecimal("29.99"), product.price());
        assertEquals(Color.BLUE, product.color());
    }

    @Test
    void roundTrip() {
        var original = new Product("p2", "Gadget", "A handy gadget", new BigDecimal("9.95"), Color.GREEN);
        var restored = Product.fromJSON(original.toJSON());

        assertEquals(original, restored);
    }

    @Test
    void zeroPriceThrowsException() {
        assertThrows(InvalidProductPriceException.class,
                () -> new Product("p3", "Bad", "Zero price", BigDecimal.ZERO, Color.GREEN));
    }

    @Test
    void negativePriceThrowsException() {
        assertThrows(InvalidProductPriceException.class,
                () -> new Product("p4", "Bad", "Negative price", new BigDecimal("-5.00"), Color.YELLOW));
    }

    @Test
    void exceptionMessageContainsPrice() {
        var price = new BigDecimal("-12.50");
        var exception = assertThrows(InvalidProductPriceException.class,
                () -> new Product("p5", "Bad", "Invalid", price, Color.BLUE));

        assertTrue(exception.getMessage().contains("-12.50"));
    }

    @Test
    void invalidColorThrowsException() {
        var json = jakarta.json.Json.createObjectBuilder()
                .add("productId", "p6")
                .add("name", "Bad")
                .add("description", "Invalid color")
                .add("price", new BigDecimal("10.00"))
                .add("color", "red")
                .build();

        assertThrows(IllegalArgumentException.class, () -> Product.fromJSON(json));
    }
}
