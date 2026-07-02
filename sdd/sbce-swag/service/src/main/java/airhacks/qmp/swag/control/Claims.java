package airhacks.qmp.swag.control;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.inventory.boundary.Reservations;
import airhacks.qmp.swag.entity.Claim;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Claims {

    static final Set<String> TSHIRT_SIZES = Set.of("XS", "S", "M", "L", "XL", "XXL");
    static final Set<String> SOCKS_SIZES = Set.of("S", "M", "L");

    final Map<String, Claim> byEmail = new ConcurrentHashMap<>();
    final Reservations reservations;

    @Inject
    Claims(Reservations reservations) {
        this.reservations = reservations;
    }

    public Claim submit(Claim claim) {
        validate(claim);
        var existing = this.byEmail.putIfAbsent(claim.email(), claim);
        if (existing != null) {
            throw ClaimRejected.duplicate(claim.email());
        }
        if (!this.reservations.reserve(claim.tShirtSize(), claim.socksSize())) {
            this.byEmail.remove(claim.email());
            throw ClaimRejected.outOfStock();
        }
        return claim;
    }

    void validate(Claim claim) {
        if (claim == null || !claim.complete()) {
            throw ClaimRejected.incomplete();
        }
        if (!claim.emailValid()) {
            throw ClaimRejected.malformedEmail();
        }
        if (!accepted(claim.tShirtSize(), TSHIRT_SIZES) || !accepted(claim.socksSize(), SOCKS_SIZES)) {
            throw ClaimRejected.invalidSize();
        }
    }

    static boolean accepted(String size, Set<String> allowed) {
        return allowed.contains(size);
    }
}
