@Violation(
    rule = "shipment requires existing payment for the customer",
    exception = ShipmentWithoutPaymentException.class
)
package airhacks.qmp.shipments;

import airhacks.qmp.Violation;
import airhacks.qmp.shipments.entity.ShipmentWithoutPaymentException;
