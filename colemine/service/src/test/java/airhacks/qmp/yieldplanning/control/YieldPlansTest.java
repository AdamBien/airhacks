package airhacks.qmp.yieldplanning.control;

import static airhacks.qmp.yieldplanning.Requirement.Rn.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.yieldplanning.Requirement;
import airhacks.qmp.yieldplanning.entity.YieldPlan;
import jakarta.ws.rs.ClientErrorException;

class YieldPlansTest {

    static LocalDate TODAY = LocalDate.of(2026, 10, 21);
    static YieldPlan PAST = new YieldPlan(TODAY.minusDays(20), 11_000);
    static YieldPlan CURRENT = new YieldPlan(TODAY, 12_500);

    YieldPlans plans = plansAt("2026-10-21T10:00:00Z");

    static YieldPlans plansAt(String instant) {
        var plans = new YieldPlans();
        plans.clock = Clock.fixed(Instant.parse(instant), ZoneOffset.UTC);
        plans.plans.put(PAST.day(), PAST);
        plans.plans.put(CURRENT.day(), CURRENT);
        return plans;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getPlanCases")
    void getPlan(Requirement.Rn requirement, LocalDate day, Optional<YieldPlan> expected) {
        assertThat(this.plans.plan(day)).as(requirement.statement()).isEqualTo(expected);
    }

    static Stream<Arguments> getPlanCases() {
        return Stream.of(
                arguments(R4_1, PAST.day(), Optional.of(PAST)),
                arguments(R4_2, TODAY.plusDays(9), Optional.empty()));
    }

    /// 22:30 UTC on Oct 21 is already 00:30 on Oct 22 in Warsaw (CEST, UTC+2).
    @Test
    @Requirement(R6_1)
    void dayStartsAtMidnightInMineZone() {
        var lateEvening = plansAt("2026-10-21T22:30:00Z");
        var started = new YieldPlan(TODAY.plusDays(1), 12_500);
        var upcoming = new YieldPlan(TODAY.plusDays(2), 12_500);

        assertThatThrownBy(() -> lateEvening.plan(started))
                .as(R6_1.statement())
                .hasMessage(YieldPlans.DAY_STARTED.formatted(started.day()));
        assertThat(lateEvening.plan(upcoming)).isEqualTo(upcoming);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("lockedDayCases")
    void lockedDay(Requirement.Rn requirement, Consumer<YieldPlans> change) {
        assertThatThrownBy(() -> change.accept(this.plans))
                .as(requirement.statement())
                .isInstanceOfSatisfying(ClientErrorException.class,
                        e -> assertThat(e.getResponse().getStatus()).isEqualTo(409))
                .hasMessage(YieldPlans.DAY_STARTED.formatted(TODAY));
        assertThat(this.plans.plan(TODAY)).contains(CURRENT);
    }

    static Stream<Arguments> lockedDayCases() {
        return Stream.of(
                arguments(R6_2, (Consumer<YieldPlans>) plans -> plans.plan(new YieldPlan(TODAY, 9_000))),
                arguments(R6_3, (Consumer<YieldPlans>) plans -> plans.revise(TODAY, 9_000)),
                arguments(R6_4, (Consumer<YieldPlans>) plans -> plans.delete(TODAY)));
    }
}
