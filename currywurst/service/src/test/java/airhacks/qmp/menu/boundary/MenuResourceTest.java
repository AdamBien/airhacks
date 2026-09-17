package airhacks.qmp.menu.boundary;

import static airhacks.qmp.menu.Requirement.Rn.R1_1;
import static airhacks.qmp.menu.Requirement.Rn.R1_2;
import static airhacks.qmp.menu.Requirement.Rn.R1_3;
import static airhacks.qmp.menu.Requirement.Rn.R2_1;
import static airhacks.qmp.menu.Requirement.Rn.R2_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.menu.Requirement;
import airhacks.qmp.menu.control.Catalog;
import airhacks.qmp.menu.entity.MenuItem;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.NotFoundException;

class MenuResourceTest {

    MenuResource menu;

    @BeforeEach
    void init() {
        this.menu = new MenuResource();
        this.menu.catalog = new Catalog();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listMenuCases")
    void listMenu(Requirement.Rn requirement, Consumer<List<MenuItem>> check) {
        var listing = this.menu.listMenu();
        check.accept(items(listing));
    }

    static Stream<Arguments> listMenuCases() {
        var required = List.of("Currywurst mit Darm", "Currywurst ohne Darm", "Pommes",
                "Currywurst mit Pommes", "Cola", "Wasser");
        return Stream.of(
                arguments(R1_1, (Consumer<List<MenuItem>>) items -> {
                    var names = items.stream().map(MenuItem::name).toList();
                    assertTrue(names.containsAll(required), R1_1 + " — " + R1_1.statement() + " missing from " + names);
                    assertTrue(items.stream().allMatch(item -> item.priceInCents() > 0), R1_1 + " — " + R1_1.statement());
                }),
                arguments(R1_2, (Consumer<List<MenuItem>>) items ->
                    assertEquals(new Catalog().items(), items, R1_2 + " — " + R1_2.statement())),
                arguments(R1_3, (Consumer<List<MenuItem>>) items ->
                    assertEquals(items, items(new MenuResource() {{ catalog = new Catalog(); }}.listMenu()),
                            R1_3 + " — " + R1_3.statement())));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getItemCases")
    void getItem(Requirement.Rn requirement, String name, boolean known) {
        if (known) {
            var item = MenuItem.fromJSON(this.menu.getItem(name));
            assertEquals(name, item.name(), requirement + " — " + requirement.statement());
            assertTrue(item.priceInCents() > 0, requirement + " — " + requirement.statement());
        } else {
            assertThrows(NotFoundException.class, () -> this.menu.getItem(name),
                    requirement + " — " + requirement.statement());
        }
    }

    static Stream<Arguments> getItemCases() {
        return Stream.of(
                arguments(R2_1, "Pommes", true),
                arguments(R2_2, "Bratwurst", false));
    }

    static List<MenuItem> items(JsonArray listing) {
        return listing.getValuesAs(JsonObject.class).stream()
                .map(MenuItem::fromJSON)
                .toList();
    }
}
