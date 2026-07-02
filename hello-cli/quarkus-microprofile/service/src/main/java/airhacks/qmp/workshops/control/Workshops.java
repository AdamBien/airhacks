package airhacks.qmp.workshops.control;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.workshops.entity.Workshop;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Maintains the scheduled airhacks.live workshops.
 */
@ApplicationScoped
public class Workshops {

    private final Map<String, Workshop> workshops = new ConcurrentHashMap<>();

    public Workshop schedule(Workshop workshop) {
        var id = Optional.ofNullable(workshop.id()).orElseGet(() -> UUID.randomUUID().toString());
        var scheduled = new Workshop(id, workshop.title(), workshop.date(), workshop.capacity());
        this.workshops.put(id, scheduled);
        return scheduled;
    }

    public Optional<Workshop> byId(String id) {
        return Optional.ofNullable(this.workshops.get(id));
    }

    public List<Workshop> all() {
        return List.copyOf(this.workshops.values());
    }
}
