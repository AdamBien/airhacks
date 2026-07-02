package airhacks.qmp.attendees.control;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.attendees.entity.Attendee;
import airhacks.qmp.workshops.control.Workshops;
import airhacks.qmp.workshops.entity.Workshop;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/**
 * Maintains the registered airhacks.live attendees and enforces workshop
 * enrollment rules.
 */
@ApplicationScoped
public class Attendees {

    private final Map<String, Attendee> attendees = new ConcurrentHashMap<>();

    @Inject
    Workshops workshops;

    public Attendee register(Attendee attendee) {
        var workshop = this.workshops.byId(attendee.workshopId())
                .orElseThrow(() -> new BadRequestException(
                        "Unknown workshop: " + attendee.workshopId()));
        this.verifyCapacity(workshop);
        var id = Optional.ofNullable(attendee.id()).orElseGet(() -> UUID.randomUUID().toString());
        var registered = new Attendee(id, attendee.name(), attendee.email(), workshop.id());
        this.attendees.put(id, registered);
        return registered;
    }

    void verifyCapacity(Workshop workshop) {
        if (this.enrolledIn(workshop.id()) >= workshop.capacity()) {
            throw new ClientErrorException(
                    "Workshop at capacity: " + workshop.id(), Response.Status.CONFLICT);
        }
    }

    public long enrolledIn(String workshopId) {
        return this.attendees.values().stream()
                .filter(attendee -> workshopId.equals(attendee.workshopId()))
                .count();
    }

    public Optional<Attendee> byId(String id) {
        return Optional.ofNullable(this.attendees.get(id));
    }

    public List<Attendee> all() {
        return List.copyOf(this.attendees.values());
    }
}
