package airhacks.qmp.ordering.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/// Black-box coverage of the `ordering` boundary; row labels are the spec's statement ids.
/// The stand runs all day, so every row places the orders it needs instead of assuming an empty book.
@QuarkusTest
public class OrdersResourceIT {

    static final int NO_SUCH_ORDER = 999_999;
    static final int POMMES_PRICE = 300;
    static final int COLA_PRICE = 250;

    @Inject
    @RestClient
    OrdersResourceClient rut;

    @ParameterizedTest(name = "{0}")
    @MethodSource("placeOrderCases")
    public void placeOrder(String requirement, JsonObject orderRequest, int expectedStatus) {
        try (var response = this.rut.placeOrder(orderRequest)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (expectedStatus != 201) {
                return;
            }
            var order = response.readEntity(JsonObject.class);
            assertThat(order.getString("state")).as(requirement).isEqualTo("placed");
            assertThat(order.getInt("number")).as(requirement).isPositive();
            assertThat(response.getHeaderString("Location")).as(requirement).endsWith("/orders/" + order.getInt("number"));
            switch (requirement) {
                case "R1.2" -> assertThat(placeAndNumber(orderRequest)).as(requirement).isEqualTo(order.getInt("number") + 1);
                case "R1.3" -> assertThat(order.getInt("totalInCents")).as(requirement).isEqualTo(2 * POMMES_PRICE + 3 * COLA_PRICE);
                default -> { }
            }
        }
    }

    static Stream<Arguments> placeOrderCases() {
        return Stream.of(
                arguments("R1.1", request(line("Pommes", 1)), 201),
                arguments("R1.2", request(line("Cola", 1)), 201),
                arguments("R1.3", request(line("Pommes", 2), line("Cola", 3)), 201),
                arguments("R1.4", request(), 400),
                arguments("R1.5", request(line("Pommes", 1), line("Bratwurst", 1)), 400),
                arguments("R1.6", request(line("Pommes", 1), line("Cola", 0)), 400));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getOrderCases")
    public void getOrder(String requirement, boolean exists, int expectedStatus) {
        var number = exists ? placeAndNumber(request(line("Pommes", 2), line("Cola", 1))) : NO_SUCH_ORDER;
        try (var response = this.rut.getOrder(number)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
            if (!exists) {
                return;
            }
            var order = response.readEntity(JsonObject.class);
            assertThat(order.getInt("number")).as(requirement).isEqualTo(number);
            assertThat(order.getJsonArray("lines")).as(requirement).hasSize(2);
            assertThat(order.getInt("totalInCents")).as(requirement).isEqualTo(2 * POMMES_PRICE + COLA_PRICE);
            assertThat(order.getString("state")).as(requirement).isEqualTo("placed");
        }
    }

    static Stream<Arguments> getOrderCases() {
        return Stream.of(
                arguments("R2.1", true, 200),
                arguments("R2.2", false, 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cancelOrderCases")
    public void cancelOrder(String requirement, boolean exists, boolean alreadyCancelled, int expectedStatus) {
        var number = exists ? placeAndNumber(request(line("Wasser", 1))) : NO_SUCH_ORDER;
        if (alreadyCancelled) {
            this.rut.cancelOrder(number).close();
        }
        try (var response = this.rut.cancelOrder(number)) {
            assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        }
        if (exists) {
            assertThat(state(number)).as(requirement).isEqualTo("cancelled");
        }
    }

    static Stream<Arguments> cancelOrderCases() {
        return Stream.of(
                arguments("R3.1", true, false, 200),
                arguments("R3.2", true, true, 409),
                arguments("R3.3", false, false, 404));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listOpenOrdersCases")
    public void listOpenOrders(String requirement, boolean expectOpen) {
        var kept = placeAndNumber(request(line("Cola", 1)));
        var cancelled = placeAndNumber(request(line("Cola", 1)));
        this.rut.cancelOrder(cancelled).close();
        if (!expectOpen) {
            numbers(this.rut.listOpenOrders()).forEach(number -> this.rut.cancelOrder(number).close());
        }
        var open = numbers(this.rut.listOpenOrders());
        if (expectOpen) {
            assertThat(open).as(requirement).contains(kept).doesNotContain(cancelled).isSorted();
        } else {
            assertThat(open).as(requirement).isEmpty();
        }
    }

    static Stream<Arguments> listOpenOrdersCases() {
        return Stream.of(
                arguments("R4.1", true),
                arguments("R4.2", false));
    }

    int placeAndNumber(JsonObject orderRequest) {
        try (var response = this.rut.placeOrder(orderRequest)) {
            return response.readEntity(JsonObject.class).getInt("number");
        }
    }

    String state(int number) {
        try (var response = this.rut.getOrder(number)) {
            return response.readEntity(JsonObject.class).getString("state");
        }
    }

    static List<Integer> numbers(JsonArray orders) {
        return orders.getValuesAs(JsonObject.class).stream()
                .map(order -> order.getInt("number"))
                .toList();
    }

    static JsonObject request(JsonObject... lines) {
        var array = Json.createArrayBuilder();
        Stream.of(lines).forEach(array::add);
        return Json.createObjectBuilder().add("lines", array).build();
    }

    static JsonObject line(String item, int quantity) {
        return Json.createObjectBuilder()
                .add("item", item)
                .add("quantity", quantity)
                .build();
    }
}
