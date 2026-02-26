package airhacks.qmp.shipments.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.qmp.shipments.entity.Shipment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentStore {

    static System.Logger LOG = System.getLogger(ShipmentStore.class.getName());

    List<Shipment> shipments = new CopyOnWriteArrayList<>();

    public List<Shipment> all() {
        LOG.log(INFO, "returning all shipments");
        return this.shipments;
    }

    public Shipment add(Shipment shipment) {
        LOG.log(INFO, "adding shipment for customer: " + shipment.customerId());
        this.shipments.add(shipment);
        return shipment;
    }
}
