package airhacks.qmp.shipments.entity;

import jakarta.ws.rs.BadRequestException;

public class ShipmentWithoutPaymentException extends BadRequestException {

    public ShipmentWithoutPaymentException(String customerId) {
        super("No payment found for customer " + customerId);
    }
}
