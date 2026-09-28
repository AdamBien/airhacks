package airhacks.hamburger.beverages.boundary;

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
class BeveragesResourceIT {

    @Inject
    @RestClient
    BeveragesResourceClient rut;

    @Test
    void listBeverages() {
        var beverages = this.rut.listBeverages();
        assertThat(beverages).as("R1.2").isNotEmpty();
        assertThat(beverages).as("R1.1").allSatisfy(beverage -> {
            assertThat(beverage.name()).isNotBlank();
            assertThat(beverage.calories()).isNotNegative();
            assertThat(beverage.priceInCents()).isPositive();
        });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findBeverageCases")
    void findBeverage(String requirement, String name, int expectedStatus) {
        try (var response = this.rut.findBeverage(name)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        }
    }

    static Stream<Arguments> findBeverageCases() {
        return Stream.of(
                arguments("R2.1", "cola", 200),
                arguments("R2.2", "unicorn-tears", 404),
                arguments("R2.3", " ", 400));
    }

    @Test
    void findBeverageAgain() {
        try (var first = this.rut.findBeverage("cola"); var second = this.rut.findBeverage("cola")) {
            assertThat(second.readEntity(String.class)).as("R2.4").isEqualTo(first.readEntity(String.class));
        }
    }
}
