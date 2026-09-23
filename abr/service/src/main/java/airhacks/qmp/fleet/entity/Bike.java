package airhacks.qmp.fleet.entity;

import static airhacks.qmp.fleet.entity.BikeStatus.ALLOCATED;
import static airhacks.qmp.fleet.entity.BikeStatus.AVAILABLE;
import static airhacks.qmp.fleet.entity.BikeStatus.WITHDRAWN;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/// One physical bicycle in the fleet. Every state transition returns a new
/// instance and refuses transitions the status does not allow.
public record Bike(String id, BikeType type, BikeStatus status) {

    public static Bike register(String id, BikeType type) {
        return new Bike(id, type, AVAILABLE);
    }

    public boolean isAvailable() {
        return this.status == AVAILABLE;
    }

    public boolean isInService() {
        return this.status != WITHDRAWN;
    }

    public Bike allocated() {
        return transition(AVAILABLE, ALLOCATED);
    }

    public Bike released() {
        return transition(ALLOCATED, AVAILABLE);
    }

    public Bike withdrawn() {
        return transition(AVAILABLE, WITHDRAWN);
    }

    public Bike restored() {
        return transition(WITHDRAWN, AVAILABLE);
    }

    Bike transition(BikeStatus from, BikeStatus to) {
        if (this.status != from) {
            throw new IllegalStateException("bike %s is %s, not %s".formatted(this.id, this.status.label(), from.label()));
        }
        return new Bike(this.id, this.type, to);
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("type", this.type.label())
                .add("status", this.status.label())
                .build();
    }
}
