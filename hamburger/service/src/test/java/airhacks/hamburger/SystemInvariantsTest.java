package airhacks.hamburger;

import static org.junit.jupiter.api.Assertions.assertTrue;

import airhacks.hamburger.beverages.control.Beverages;
import airhacks.hamburger.beverages.entity.Beverage;
import airhacks.hamburger.menu.control.Menu;
import airhacks.hamburger.menu.entity.MenuItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/// Tests for the `## System invariants` of the system doc in [airhacks.hamburger]; each test embeds its `Sn` id.
class SystemInvariantsTest {

    @Test
    @DisplayName("S1")
    void s1NoBeverageSharesANameWithAMenuItem() {
        var menuNames = new Menu().items().stream().map(MenuItem::name).collect(Collectors.toSet());
        var shared = new Beverages().all().stream().map(Beverage::name).filter(menuNames::contains).collect(Collectors.toSet());
        assertTrue(shared.isEmpty(), "S1 — The system shall never offer a beverage and a menu item under the same name; shared: " + shared);
    }
}
