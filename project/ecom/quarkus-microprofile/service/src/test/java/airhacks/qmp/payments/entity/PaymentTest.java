package airhacks.qmp.payments.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import jakarta.json.JsonObject;

import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void toJSON() {
        var payment = new Payment("c1", new BigDecimal("19.99"), "order");
        var json = payment.toJSON();

        assertEquals("c1", json.getString("customerId"));
        assertEquals(new BigDecimal("19.99"), json.getJsonNumber("amount").bigDecimalValue());
        assertEquals("order", json.getString("description"));
    }

    @Test
    void fromJSON() {
        var json = jakarta.json.Json.createObjectBuilder()
                .add("customerId", "c1")
                .add("amount", new BigDecimal("19.99"))
                .add("description", "order")
                .build();

        var payment = Payment.fromJSON(json);

        assertEquals("c1", payment.customerId());
        assertEquals(new BigDecimal("19.99"), payment.amount());
        assertEquals("order", payment.description());
    }

    @Test
    void roundTrip() {
        var original = new Payment("c2", new BigDecimal("0.10"), "refund");
        var restored = Payment.fromJSON(original.toJSON());

        assertEquals(original, restored);
    }
}
