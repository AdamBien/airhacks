package airhacks.qmp.yieldplanning.boundary;

import static airhacks.qmp.yieldplanning.Requirement.Rn.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.yieldplanning.Requirement;
import airhacks.qmp.yieldplanning.control.YieldPlans;
import airhacks.qmp.yieldplanning.entity.YieldPlan;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

class YieldPlansResourceTest {

    static LocalDate TODAY = LocalDate.now(ZoneId.of("Europe/Warsaw"));
    static LocalDate DAY = TODAY.plusDays(10);

    YieldPlansResource resource = resource();

    record Outcome(int status, Object body) {

        static Outcome of(Supplier<?> act) {
            try {
                return switch (act.get()) {
                    case null -> new Outcome(204, null);
                    case Response response -> new Outcome(response.getStatus(), response.getEntity());
                    case Object body -> new Outcome(200, body);
                };
            } catch (WebApplicationException e) {
                var response = e.getResponse();
                var body = response.getEntity() == null ? e.getMessage() : response.getEntity();
                return new Outcome(response.getStatus(), body);
            }
        }
    }

    static YieldPlansResource resource() {
        var resource = new YieldPlansResource();
        resource.plans = new YieldPlans();
        return resource;
    }

    static JsonObject plan(LocalDate day, long tonnes) {
        return plan(day, tonnes, 50);
    }

    static JsonObject plan(LocalDate day, long tonnes, int stoneSize) {
        return new YieldPlan(day, tonnes, stoneSize).toJSON();
    }

    static JsonObject revision(Map<String, Object> fields) {
        return Json.createObjectBuilder(fields).build();
    }

    void preplan(LocalDate... days) {
        Stream.of(days)
                .map(day -> plan(day, 12_500))
                .forEach(this.resource::planDay);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("planDayCases")
    void planDay(Requirement.Rn requirement, List<LocalDate> preplanned, JsonObject input, Outcome expected) {
        preplan(preplanned.toArray(LocalDate[]::new));
        var outcome = Outcome.of(() -> this.resource.planDay(input));
        assertThat(outcome).as(requirement.statement()).isEqualTo(expected);
    }

    static Stream<Arguments> planDayCases() {
        var invalid = Json.createObjectBuilder()
                .add(YieldPlan.TONNES, 1.5)
                .add(YieldPlan.STONE_SIZE, 0)
                .build();
        return Stream.of(
                arguments(R1_1, List.of(), plan(DAY, 12_500, 80), new Outcome(201, plan(DAY, 12_500, 80))),
                arguments(R1_2, List.of(), plan(DAY, 0), new Outcome(201, plan(DAY, 0))),
                arguments(R1_3, List.of(DAY), plan(DAY, 9_000), new Outcome(409, "day already planned: " + DAY)),
                arguments(R1_4, List.of(), invalid, new Outcome(400, """
                        {"invalid":["day","tonnes","stoneSize"]}""")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("revisePlanCases")
    void revisePlan(Requirement.Rn requirement, boolean preplanned, JsonObject input, Outcome expected) {
        if (preplanned)
            preplan(DAY);
        var outcome = Outcome.of(() -> this.resource.revisePlan(DAY.toString(), input));
        assertThat(outcome).as(requirement.statement()).isEqualTo(expected);
    }

    static Stream<Arguments> revisePlanCases() {
        return Stream.of(
                arguments(R2_1, true, revision(Map.of(YieldPlan.TONNES, 9_000, YieldPlan.STONE_SIZE, 80)),
                        new Outcome(200, plan(DAY, 9_000, 80))),
                arguments(R2_2, false, revision(Map.of(YieldPlan.TONNES, 9_000)),
                        new Outcome(404, "day not planned: " + DAY)),
                arguments(R2_4, true, revision(Map.of(YieldPlan.STONE_SIZE, 80)),
                        new Outcome(200, plan(DAY, 12_500, 80))),
                arguments(R2_5, true, revision(Map.of(YieldPlan.TONNES, 9_000)),
                        new Outcome(200, plan(DAY, 9_000, 50))),
                arguments(R2_6, true, revision(Map.of()), new Outcome(400, """
                        {"invalid":["tonnes","stoneSize"]}""")),
                arguments(R2_7, true, revision(Map.of(YieldPlan.TONNES, -1, YieldPlan.STONE_SIZE, 2.5)),
                        new Outcome(400, """
                                {"invalid":["tonnes","stoneSize"]}""")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deletePlanCases")
    void deletePlan(Requirement.Rn requirement, boolean preplanned, Outcome expected) {
        if (preplanned)
            preplan(DAY);
        var outcome = Outcome.of(() -> {
            this.resource.deletePlan(DAY.toString());
            return null;
        });
        assertThat(outcome).as(requirement.statement()).isEqualTo(expected);
        assertThat(Outcome.of(() -> this.resource.getPlan(DAY.toString())).status()).isEqualTo(404);
    }

    static Stream<Arguments> deletePlanCases() {
        return Stream.of(
                arguments(R3_1, true, new Outcome(204, null)),
                arguments(R3_2, false, new Outcome(404, "day not planned: " + DAY)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listPlansCases")
    void listPlans(Requirement.Rn requirement, String from, String to, Outcome expected) {
        preplan(DAY.plusDays(5), DAY.plusDays(1), DAY.plusDays(3), DAY.plusDays(10));
        var outcome = Outcome.of(() -> this.resource.listPlans(from, to));
        assertThat(outcome).as(requirement.statement()).isEqualTo(expected);
    }

    static Stream<Arguments> listPlansCases() {
        return Stream.of(
                arguments(R5_1, DAY.plusDays(1).toString(), DAY.plusDays(5).toString(),
                        new Outcome(200, plans(DAY.plusDays(1), DAY.plusDays(3), DAY.plusDays(5)))),
                arguments(R5_2, DAY.plusDays(2).toString(), DAY.plusDays(4).toString(),
                        new Outcome(200, plans(DAY.plusDays(3)))),
                arguments(R5_3, DAY.plusDays(5).toString(), DAY.plusDays(1).toString(),
                        new Outcome(400, "from %s is after to %s".formatted(DAY.plusDays(5), DAY.plusDays(1)))),
                arguments(R5_4, null, "next-friday", new Outcome(400, """
                        {"invalid":["from","to"]}""")));
    }

    static JsonArray plans(LocalDate... days) {
        return Stream.of(days)
                .map(day -> plan(day, 12_500))
                .collect(JsonCollectors.toJsonArray());
    }
}
