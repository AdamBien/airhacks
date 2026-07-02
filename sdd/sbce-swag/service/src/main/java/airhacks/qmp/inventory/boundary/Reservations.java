package airhacks.qmp.inventory.boundary;

import airhacks.qmp.inventory.control.Inventory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/// In-process entry point for `reserve-swag`, consumed by the swag component when a claim is
/// accepted. Kept separate from the HTTP resource: reservation is an internal call between
/// business components, never an external endpoint.
@ApplicationScoped
public class Reservations {

    final Inventory inventory;

    @Inject
    public Reservations(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean reserve(String tShirtSize, String socksSize) {
        return this.inventory.reserve(tShirtSize, socksSize);
    }
}
