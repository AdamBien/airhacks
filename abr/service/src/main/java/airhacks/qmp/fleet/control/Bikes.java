package airhacks.qmp.fleet.control;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

import airhacks.qmp.fleet.entity.Bike;
import airhacks.qmp.fleet.entity.BikeType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

/// In-memory fleet. The public methods are the cross-BC contract used by
/// `rental` (allocate, release) and `reservation` (count).
@ApplicationScoped
public class Bikes {

    final Map<String, Bike> bikes = new ConcurrentHashMap<>();

    public Bike registerBike(String id, String typeLabel) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("bike id is required");
        }
        var bike = Bike.register(id, typeOf(typeLabel));
        if (this.bikes.putIfAbsent(id, bike) != null) {
            throw new BikeConflict("bike already registered: " + id);
        }
        return bike;
    }

    public List<Bike> listAvailableBikes(Optional<BikeType> type) {
        return this.bikes.values().stream()
                .filter(Bike::isAvailable)
                .filter(bike -> type.map(bike.type()::equals).orElse(true))
                .sorted((a, b) -> a.id().compareTo(b.id()))
                .toList();
    }

    public long countBikes(Optional<BikeType> type) {
        return this.bikes.values().stream()
                .filter(Bike::isInService)
                .filter(bike -> type.map(bike.type()::equals).orElse(true))
                .count();
    }

    public Bike withdrawBike(String id) {
        return transition(id, Bike::withdrawn);
    }

    public Bike restoreBike(String id) {
        return transition(id, Bike::restored);
    }

    public synchronized Bike allocateBike(BikeType type) {
        var candidate = listAvailableBikes(Optional.of(type)).stream()
                .findFirst()
                .orElseThrow(() -> new BikeConflict("no available bike of type " + type.label()));
        return transition(candidate.id(), Bike::allocated);
    }

    public Bike releaseBike(String id) {
        return transition(id, Bike::released);
    }

    public static BikeType typeOf(String label) {
        return BikeType.of(label)
                .orElseThrow(() -> new BadRequestException("unknown bike type: " + label));
    }

    public static Optional<BikeType> optionalTypeOf(String label) {
        return Optional.ofNullable(label).map(Bikes::typeOf);
    }

    Bike transition(String id, UnaryOperator<Bike> transition) {
        try {
            var next = this.bikes.computeIfPresent(id, (_, current) -> transition.apply(current));
            if (next == null) {
                throw new NotFoundException("unknown bike: " + id);
            }
            return next;
        } catch (IllegalStateException e) {
            throw new BikeConflict(e.getMessage());
        }
    }
}
