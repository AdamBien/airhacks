package airhacks.hamburger.menu.boundary;

import static airhacks.hamburger.menu.Requirement.Rn.R1_1;
import static airhacks.hamburger.menu.Requirement.Rn.R1_2;
import static airhacks.hamburger.menu.Requirement.Rn.R2_1;
import static airhacks.hamburger.menu.Requirement.Rn.R2_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import airhacks.hamburger.menu.Requirement;
import airhacks.hamburger.menu.control.Menu;
import airhacks.hamburger.menu.entity.MenuItem;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuItemsResourceTest {

    MenuItemsResource rut;

    @BeforeEach
    void init() {
        this.rut = new MenuItemsResource();
        this.rut.menu = new Menu();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listMenuCases")
    void listMenu(Requirement.Rn requirement, Predicate<List<MenuItem>> expectation) {
        @SuppressWarnings("unchecked")
        var items = (List<MenuItem>) this.rut.listMenu().getEntity();
        assertTrue(expectation.test(items), requirement + " — " + requirement.statement());
    }

    static Stream<Arguments> listMenuCases() {
        return Stream.of(
                arguments(R1_1, (Predicate<List<MenuItem>>) MenuItemsResourceTest::everyItemNamedAndPriced),
                arguments(R1_2, (Predicate<List<MenuItem>>) items -> !items.isEmpty()));
    }

    static boolean everyItemNamedAndPriced(List<MenuItem> items) {
        return items.stream().allMatch(MenuItemsResourceTest::namedAndPriced);
    }

    static boolean namedAndPriced(MenuItem item) {
        return !item.name().isBlank() && item.calories() > 0 && item.priceInCents() > 0;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findItemCases")
    void findItem(Requirement.Rn requirement, String name, boolean exists) {
        if (!exists) {
            assertThrows(NotFoundException.class, () -> this.rut.findItem(name), requirement + " — " + requirement.statement());
            return;
        }
        var item = (MenuItem) this.rut.findItem(name).getEntity();
        assertEquals(name, item.name(), requirement + " — " + requirement.statement());
        assertTrue(item.calories() > 0, requirement + " — " + requirement.statement());
        assertTrue(item.priceInCents() > 0, requirement + " — " + requirement.statement());
    }

    static Stream<Arguments> findItemCases() {
        return Stream.of(
                arguments(R2_1, "cheese", true),
                arguments(R2_2, "unicorn", false));
    }
}
