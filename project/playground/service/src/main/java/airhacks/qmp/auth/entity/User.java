package airhacks.qmp.auth.entity;

import java.time.Instant;

public record User(String id, String email, String name, String passwordHash, String salt, Role role, Instant createdAt) {
}
