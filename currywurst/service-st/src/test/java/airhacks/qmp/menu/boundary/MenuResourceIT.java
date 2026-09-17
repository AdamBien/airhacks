package airhacks.qmp.menu.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

/// Black-box coverage of the `menu` boundary; row labels are the spec's statement ids.
@QuarkusTest
public class MenuResourceIT {

    @Inject
    @RestClient
    MenuResourceClient rut;

    @ParameterizedTest(name = "{0}")
    @MethodSource("listMenuCases")
    public void listMenu(String requirement, String requiredItem) {
        var listing = this.rut.listMenu();
        var names = listing.getValuesAs(JsonObject.class).stream()
                .map(item -> item.getString("name"))
                .toList();
        assertThat(names).as(requirement).contains(requiredItem);
        assertThat(this.rut.listMenu()).as("R1.3").isEqualTo(listing);
    }

    static Stream<Arguments> listMenuCases() {
        return Stream.of(
                arguments("R1.1", "Currywurst mit Darm"),
                arguments("R1.2", "Wasser"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getItemCases")
    public void getItem(String requirement, String name, int expectedStatus) {
        try (var response = this.rut.getItem(name)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        }
    }

    static Stream<Arguments> getItemCases() {
        return Stream.of(
                arguments("R2.1", "Pommes", 200),
                arguments("R2.2", "Bratwurst", 404));
    }
}
