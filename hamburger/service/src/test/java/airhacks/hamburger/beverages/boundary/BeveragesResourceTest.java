package airhacks.hamburger.beverages.boundary;

import static airhacks.hamburger.beverages.Requirement.Rn.R1_1;
import static airhacks.hamburger.beverages.Requirement.Rn.R1_2;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_1;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_2;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_3;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import airhacks.hamburger.beverages.Requirement;
import airhacks.hamburger.beverages.control.Beverages;
import airhacks.hamburger.beverages.entity.Beverage;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BeveragesResourceTest {

    enum Outcome { FOUND, NOT_FOUND, REJECTED, SAME_AGAIN }

    BeveragesResource rut;

    @BeforeEach
    void init() {
        this.rut = new BeveragesResource();
        this.rut.beverages = new Beverages();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listBeveragesCases")
    void listBeverages(Requirement.Rn requirement, Predicate<List<Beverage>> expectation) {
        @SuppressWarnings("unchecked")
        var beverages = (List<Beverage>) this.rut.listBeverages().getEntity();
        assertTrue(expectation.test(beverages), requirement + " — " + requirement.statement());
    }

    static Stream<Arguments> listBeveragesCases() {
        return Stream.of(
                arguments(R1_1, (Predicate<List<Beverage>>) BeveragesResourceTest::everyBeverageNamedAndPriced),
                arguments(R1_2, (Predicate<List<Beverage>>) beverages -> !beverages.isEmpty()));
    }

    static boolean everyBeverageNamedAndPriced(List<Beverage> beverages) {
        return beverages.stream().allMatch(BeveragesResourceTest::namedAndPriced);
    }

    static boolean namedAndPriced(Beverage beverage) {
        return !beverage.name().isBlank() && beverage.calories() >= 0 && beverage.priceInCents() > 0;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findBeverageCases")
    void findBeverage(Requirement.Rn requirement, String name, Outcome outcome) {
        var message = requirement + " — " + requirement.statement();
        switch (outcome) {
            case FOUND -> assertEquals(name, found(name).name(), message);
            case NOT_FOUND -> assertThrows(NotFoundException.class, () -> this.rut.findBeverage(name), message);
            case REJECTED -> assertThrows(BadRequestException.class, () -> this.rut.findBeverage(name), message);
            case SAME_AGAIN -> assertEquals(found(name), found(name), message);
        }
    }

    Beverage found(String name) {
        return (Beverage) this.rut.findBeverage(name).getEntity();
    }

    static Stream<Arguments> findBeverageCases() {
        return Stream.of(
                arguments(R2_1, "cola", Outcome.FOUND),
                arguments(R2_2, "unicorn-tears", Outcome.NOT_FOUND),
                arguments(R2_3, " ", Outcome.REJECTED),
                arguments(R2_4, "cola", Outcome.SAME_AGAIN));
    }
}
