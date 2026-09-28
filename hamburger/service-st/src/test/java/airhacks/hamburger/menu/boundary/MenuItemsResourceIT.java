package airhacks.hamburger.menu.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.stream.Stream;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@QuarkusTest
class MenuItemsResourceIT {

    @Inject
    @RestClient
    MenuItemsResourceClient rut;

    @Test
    void listMenu() {
        var items = this.rut.listMenu();
        assertThat(items).as("R1.2").isNotEmpty();
        assertThat(items).as("R1.1").allSatisfy(item -> {
            assertThat(item.name()).isNotBlank();
            assertThat(item.calories()).isPositive();
            assertThat(item.priceInCents()).isPositive();
        });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findItemCases")
    void findItem(String requirement, String name, int expectedStatus) {
        try (var response = this.rut.findItem(name)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        }
    }

    static Stream<Arguments> findItemCases() {
        return Stream.of(
                arguments("R2.1", "cheese", 200),
                arguments("R2.2", "unicorn", 404));
    }
}
