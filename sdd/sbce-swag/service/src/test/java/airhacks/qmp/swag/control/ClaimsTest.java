package airhacks.qmp.swag.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.inventory.boundary.Reservations;
import airhacks.qmp.inventory.control.Inventory;
import airhacks.qmp.inventory.entity.SwagItem;
import airhacks.qmp.swag.entity.Claim;
import airhacks.qmp.swag.entity.ShippingAddress;

class ClaimsTest {

    Claims claims;

    @BeforeEach
    void init() {
        this.claims = stockedClaims();
    }

    static Claims stockedClaims() {
        var inventory = new Inventory();
        inventory.set(SwagItem.T_SHIRT, "L", 100);
        inventory.set(SwagItem.SOCKS, "M", 100);
        return new Claims(new Reservations(inventory));
    }

    static ShippingAddress address() {
        return new ShippingAddress("Duke", "Java", "1 Java Street", "12345", "Palo Alto", "USA");
    }

    static Claim valid() {
        return new Claim("duke@airhacks.live", "L", "M", address());
    }

    /// Covers R1.1, R1.2, R1.3, R1.4 of the swag capability.
    static Stream<Arguments> r1ClaimSwag() {
        return Stream.of(
                arguments("R1.1 accepts a complete claim", valid(), true),
                arguments("R1.2 rejects a missing email", new Claim("", "L", "M", address()), false),
                arguments("R1.2 rejects a missing t-shirt size", new Claim("duke@airhacks.live", null, "M", address()), false),
                arguments("R1.2 rejects a missing socks size", new Claim("duke@airhacks.live", "L", " ", address()), false),
                arguments("R1.2 rejects an incomplete address",
                        new Claim("duke@airhacks.live", "L", "M", new ShippingAddress("Duke", "Java", "1 Java Street", "", "Palo Alto", "USA")), false),
                arguments("R1.3 rejects a malformed email", new Claim("duke(at)airhacks", "L", "M", address()), false),
                arguments("R1.4 rejects an unaccepted t-shirt size", new Claim("duke@airhacks.live", "XXXL", "M", address()), false),
                arguments("R1.4 rejects an unaccepted socks size", new Claim("duke@airhacks.live", "L", "XL", address()), false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("r1ClaimSwag")
    void claimSwag(String requirement, Claim claim, boolean accepted) {
        if (accepted) {
            var confirmed = this.claims.submit(claim);
            assertEquals(claim.email(), confirmed.email());
        } else {
            assertThrows(ClaimRejected.class, () -> this.claims.submit(claim));
        }
    }

    @Test
    void rejectsClaimWhenOutOfStock() { // R1.5
        var noStock = new Claims(new Reservations(new Inventory()));
        var rejected = assertThrows(ClaimRejected.class, () -> noStock.submit(valid()));
        assertEquals(400, rejected.getResponse().getStatus());
    }

    /// Covers R2.1 of the swag capability.
    @ParameterizedTest(name = "{0}")
    @MethodSource("r2OneClaimPerAttendee")
    void oneClaimPerAttendee(String requirement, Claim first, Claim second) {
        this.claims.submit(first);
        var rejected = assertThrows(ClaimRejected.class, () -> this.claims.submit(second));
        assertEquals(409, rejected.getResponse().getStatus());
    }

    static Stream<Arguments> r2OneClaimPerAttendee() {
        return Stream.of(
                arguments("R2.1 rejects a second claim for the same email", valid(),
                        new Claim("duke@airhacks.live", "S", "L", address())));
    }
}
