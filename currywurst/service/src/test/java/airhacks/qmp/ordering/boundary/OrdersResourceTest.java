package airhacks.qmp.ordering.boundary;

import static airhacks.qmp.ordering.Requirement.Rn.R1_1;
import static airhacks.qmp.ordering.Requirement.Rn.R1_2;
import static airhacks.qmp.ordering.Requirement.Rn.R1_3;
import static airhacks.qmp.ordering.Requirement.Rn.R1_4;
import static airhacks.qmp.ordering.Requirement.Rn.R1_5;
import static airhacks.qmp.ordering.Requirement.Rn.R1_6;
import static airhacks.qmp.ordering.Requirement.Rn.R2_1;
import static airhacks.qmp.ordering.Requirement.Rn.R2_2;
import static airhacks.qmp.ordering.Requirement.Rn.R3_1;
import static airhacks.qmp.ordering.Requirement.Rn.R3_2;
import static airhacks.qmp.ordering.Requirement.Rn.R3_3;
import static airhacks.qmp.ordering.Requirement.Rn.R4_1;
import static airhacks.qmp.ordering.Requirement.Rn.R4_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.menu.control.Catalog;
import airhacks.qmp.ordering.Requirement;
import airhacks.qmp.ordering.control.OrderBook;
import airhacks.qmp.ordering.entity.OrderState;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

class OrdersResourceTest {

    static final String PLACED = OrderState.PLACED.label();
    static final String CANCELLED = OrderState.CANCELLED.label();
    static final int NO_SUCH_ORDER = 4711;

    OrdersResource orders;
    OrderBook orderBook;

    @BeforeEach
    void init() {
        this.orderBook = new OrderBook();
        this.orderBook.catalog = new Catalog();
        this.orders = new OrdersResource();
        this.orders.orderBook = this.orderBook;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("placeOrderCases")
    void placeOrder(Requirement.Rn requirement, JsonObject orderRequest, boolean accepted, Consumer<OrdersResourceTest> check) {
        var message = requirement + " — " + requirement.statement();
        if (accepted) {
            var order = place(orderRequest);
            assertEquals(PLACED, order.getString("state"), message);
            assertTrue(order.getInt("number") >= 1, message);
            check.accept(this);
        } else {
            assertThrows(BadRequestException.class, () -> place(orderRequest), message);
            assertEquals(List.of(), this.orderBook.open(), message);
        }
    }

    static Stream<Arguments> placeOrderCases() {
        Consumer<OrdersResourceTest> none = _ -> {};
        return Stream.of(
                arguments(R1_1, request(line("Pommes", 1)), true, none),
                arguments(R1_2, request(line("Cola", 1)), true, (Consumer<OrdersResourceTest>) OrdersResourceTest::numbersRestartEachDay),
                arguments(R1_3, request(line("Pommes", 2), line("Cola", 3)), true, (Consumer<OrdersResourceTest>) OrdersResourceTest::pricesLinesAndTotal),
                arguments(R1_4, request(), false, none),
                arguments(R1_5, request(line("Pommes", 1), line("Bratwurst", 1)), false, none),
                arguments(R1_6, request(line("Pommes", 1), line("Cola", 0)), false, none));
    }

    void numbersRestartEachDay() {
        var message = R1_2 + " — " + R1_2.statement();
        assertEquals(1, this.orderBook.open().getFirst().number(), message);
        assertEquals(2, place(request(line("Cola", 1))).getInt("number"), message);
        this.orderBook.today = () -> LocalDate.now().plusDays(1);
        assertEquals(1, place(request(line("Wasser", 1))).getInt("number"), message);
    }

    void pricesLinesAndTotal() {
        var message = R1_3 + " — " + R1_3.statement();
        var order = this.orderBook.find(1).orElseThrow();
        var pommes = order.lines().getFirst();
        var cola = order.lines().getLast();
        assertEquals(300 * 2, pommes.totalInCents(), message);
        assertEquals(250 * 3, cola.totalInCents(), message);
        assertEquals(300 * 2 + 250 * 3, order.totalInCents(), message);
        assertEquals(order.totalInCents(), order.toJSON().getInt("totalInCents"), message);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getOrderCases")
    void getOrder(Requirement.Rn requirement, boolean exists) {
        var message = requirement + " — " + requirement.statement();
        if (exists) {
            var placed = place(request(line("Pommes", 2), line("Cola", 1)));
            var found = object(this.orders.getOrder(placed.getInt("number")));
            assertEquals(placed, found, message);
            assertEquals(2, found.getJsonArray("lines").size(), message);
            assertEquals(850, found.getInt("totalInCents"), message);
            assertEquals(PLACED, found.getString("state"), message);
        } else {
            assertThrows(NotFoundException.class, () -> this.orders.getOrder(NO_SUCH_ORDER), message);
        }
    }

    static Stream<Arguments> getOrderCases() {
        return Stream.of(
                arguments(R2_1, true),
                arguments(R2_2, false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cancelOrderCases")
    void cancelOrder(Requirement.Rn requirement, OrderState stateBefore, Status expected) {
        var message = requirement + " — " + requirement.statement();
        if (stateBefore == null) {
            assertThrows(NotFoundException.class, () -> this.orders.cancelOrder(NO_SUCH_ORDER), message);
            return;
        }
        var number = place(request(line("Pommes", 1))).getInt("number");
        var order = this.orderBook.find(number).orElseThrow();
        if (stateBefore != OrderState.PLACED) {
            order.cancel();
            assertEquals(OrderState.CANCELLED, order.state(), "precondition: order has left placed");
        }
        if (expected == Status.OK) {
            var cancelled = object(this.orders.cancelOrder(number));
            assertEquals(CANCELLED, cancelled.getString("state"), message);
            assertEquals(OrderState.CANCELLED, order.state(), message);
        } else {
            var rejected = assertThrows(ClientErrorException.class, () -> this.orders.cancelOrder(number), message);
            assertEquals(expected.getStatusCode(), rejected.getResponse().getStatus(), message);
            assertEquals(stateBefore, order.state(), message);
        }
    }

    static Stream<Arguments> cancelOrderCases() {
        return Stream.of(
                arguments(R3_1, OrderState.PLACED, Status.OK),
                arguments(R3_2, OrderState.CANCELLED, Status.CONFLICT),
                arguments(R3_3, null, Status.NOT_FOUND));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listOpenOrdersCases")
    void listOpenOrders(Requirement.Rn requirement, int placed, int cancelled, List<Integer> expectedNumbers) {
        var message = requirement + " — " + requirement.statement();
        for (var i = 0; i < placed; i++) {
            place(request(line("Wasser", 1)));
        }
        for (var number = 1; number <= cancelled; number++) {
            this.orders.cancelOrder(number);
        }
        var open = array(this.orders.listOpenOrders());
        assertEquals(expectedNumbers, numbers(open), message);
    }

    static Stream<Arguments> listOpenOrdersCases() {
        return Stream.of(
                arguments(R4_1, 3, 1, List.of(2, 3)),
                arguments(R4_2, 2, 2, List.of()));
    }

    JsonObject place(JsonObject orderRequest) {
        return object(this.orders.placeOrder(orderRequest));
    }

    static JsonObject object(Response response) {
        return (JsonObject) response.getEntity();
    }

    static JsonArray array(Response response) {
        return (JsonArray) response.getEntity();
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
