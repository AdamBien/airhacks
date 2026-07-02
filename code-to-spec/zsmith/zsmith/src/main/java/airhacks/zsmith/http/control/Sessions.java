package airhacks.zsmith.http.control;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class Sessions {

    public static final String HEADER = "X-Session-Id";

    final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public String resolveOrCreate(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }

    public ReentrantLock lockFor(String sessionId) {
        return this.locks.computeIfAbsent(sessionId, id -> new ReentrantLock());
    }
}
