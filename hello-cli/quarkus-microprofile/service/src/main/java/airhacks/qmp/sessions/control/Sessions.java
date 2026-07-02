package airhacks.qmp.sessions.control;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.sessions.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Maintains the sessions for airhacks.live workshops.
 */
@ApplicationScoped
public class Sessions {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public Session add(Session session) {
        var id = Optional.ofNullable(session.id()).orElseGet(() -> UUID.randomUUID().toString());
        var added = new Session(id, session.title(), session.speaker(), session.duration(), session.workshopId());
        this.sessions.put(id, added);
        return added;
    }

    public Optional<Session> byId(String id) {
        return Optional.ofNullable(this.sessions.get(id));
    }

    public List<Session> all() {
        return List.copyOf(this.sessions.values());
    }

    public List<Session> byWorkshop(String workshopId) {
        return this.sessions.values().stream()
                .filter(s -> s.workshopId().equals(workshopId))
                .toList();
    }
}
