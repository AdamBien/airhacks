package airhacks.qmp.yieldplanning.control;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.UnaryOperator;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.qmp.yieldplanning.entity.YieldPlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class YieldPlans {

    static final String DAY_STARTED = "day has started: %s";
    static final String ALREADY_PLANNED = "day already planned: %s";
    static final String NOT_PLANNED = "day not planned: %s";
    static final String DEFAULT_MINE_ZONE = "Europe/Warsaw";

    @Inject
    @ConfigProperty(name = "mine.zone", defaultValue = DEFAULT_MINE_ZONE)
    ZoneId mineZone = ZoneId.of(DEFAULT_MINE_ZONE);

    Clock clock = Clock.systemUTC();

    ConcurrentSkipListMap<LocalDate, YieldPlan> plans = new ConcurrentSkipListMap<>();

    public YieldPlan plan(YieldPlan plan) {
        rejectStarted(plan.day());
        if (this.plans.putIfAbsent(plan.day(), plan) != null)
            throw new ClientErrorException(ALREADY_PLANNED.formatted(plan.day()), Status.CONFLICT);
        return plan;
    }

    public YieldPlan revise(LocalDate day, UnaryOperator<YieldPlan> revision) {
        rejectStarted(day);
        var revised = this.plans.computeIfPresent(day, (_, plan) -> revision.apply(plan));
        if (revised == null)
            throw notPlanned(day);
        return revised;
    }

    public void delete(LocalDate day) {
        rejectStarted(day);
        if (this.plans.remove(day) == null)
            throw notPlanned(day);
    }

    public Optional<YieldPlan> plan(LocalDate day) {
        return Optional.ofNullable(this.plans.get(day));
    }

    public List<YieldPlan> plans(LocalDate from, LocalDate to) {
        if (from.isAfter(to))
            throw new BadRequestException("from %s is after to %s".formatted(from, to));
        return List.copyOf(this.plans.subMap(from, true, to, true).values());
    }

    /// A day starts at midnight in the mine's zone, so today is already locked.
    boolean hasStarted(LocalDate day) {
        return !day.isAfter(LocalDate.ofInstant(this.clock.instant(), this.mineZone));
    }

    void rejectStarted(LocalDate day) {
        if (hasStarted(day))
            throw new ClientErrorException(DAY_STARTED.formatted(day), Status.CONFLICT);
    }

    static NotFoundException notPlanned(LocalDate day) {
        return new NotFoundException(NOT_PLANNED.formatted(day));
    }
}
