package airhacks.qmp.inventory.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.inventory.entity.SwagItem;

class InventoryTest {

    Inventory inventory;

    @BeforeEach
    void init() {
        this.inventory = new Inventory();
    }

    int remaining(SwagItem item, String size) {
        return this.inventory.availability().stream()
                .filter(stock -> stock.item() == item && stock.size().equals(size))
                .findFirst().orElseThrow().quantity();
    }

    /// Covers R1.1, R1.2, R1.3 of the inventory capability.
    static Stream<Arguments> r1SetStock() {
        return Stream.of(
                arguments("R1.1 records stock for a recognised item and size", SwagItem.T_SHIRT, "L", 10, true),
                arguments("R1.2 rejects a negative quantity", SwagItem.T_SHIRT, "L", -1, false),
                arguments("R1.3 rejects a size the item does not offer", SwagItem.SOCKS, "XL", 5, false),
                arguments("R1.3 rejects an unknown t-shirt size", SwagItem.T_SHIRT, "XXXL", 5, false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("r1SetStock")
    void setStock(String requirement, SwagItem item, String size, int quantity, boolean accepted) {
        if (accepted) {
            this.inventory.set(item, size, quantity);
            assertEquals(quantity, remaining(item, size));
        } else {
            assertThrows(StockRejected.class, () -> this.inventory.set(item, size, quantity));
        }
    }

    @Test
    void reserveDecrementsBothSizes() { // R2.1
        this.inventory.set(SwagItem.T_SHIRT, "L", 2);
        this.inventory.set(SwagItem.SOCKS, "M", 2);
        assertTrue(this.inventory.reserve("L", "M"));
        assertEquals(1, remaining(SwagItem.T_SHIRT, "L"));
        assertEquals(1, remaining(SwagItem.SOCKS, "M"));
    }

    @Test
    void reserveRejectsWhenSoldOutAndKeepsStock() { // R2.2
        this.inventory.set(SwagItem.T_SHIRT, "L", 1);
        this.inventory.set(SwagItem.SOCKS, "M", 0);
        assertFalse(this.inventory.reserve("L", "M"));
        assertEquals(1, remaining(SwagItem.T_SHIRT, "L"));
        assertEquals(0, remaining(SwagItem.SOCKS, "M"));
    }

    @Test
    void reportsRemainingPerItemAndSize() { // R3.1
        this.inventory.set(SwagItem.T_SHIRT, "L", 3);
        this.inventory.set(SwagItem.SOCKS, "S", 4);
        assertEquals(2, this.inventory.availability().size());
        assertEquals(3, remaining(SwagItem.T_SHIRT, "L"));
        assertEquals(4, remaining(SwagItem.SOCKS, "S"));
    }
}
