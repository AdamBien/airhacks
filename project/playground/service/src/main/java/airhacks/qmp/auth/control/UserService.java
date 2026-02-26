package airhacks.qmp.auth.control;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.auth.entity.RegistrationRequest;
import airhacks.qmp.auth.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();
    final ConcurrentHashMap<String, String> resetTokens = new ConcurrentHashMap<>();

    @Inject
    PasswordService passwordService;

    public Optional<User> register(RegistrationRequest request) {
        if (this.usersByEmail.containsKey(request.email())) {
            return Optional.empty();
        }
        var salt = this.passwordService.generateSalt();
        var hash = this.passwordService.hash(request.password(), salt);
        var user = new User(
                UUID.randomUUID().toString(),
                request.email(),
                request.name(),
                hash,
                salt,
                request.role(),
                Instant.now());
        this.usersByEmail.put(request.email(), user);
        return Optional.of(user);
    }

    public Optional<User> authenticate(String email, String password) {
        var user = this.usersByEmail.get(email);
        if (user == null) {
            return Optional.empty();
        }
        if (!this.passwordService.verify(password, user.passwordHash(), user.salt())) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(this.usersByEmail.get(email));
    }

    public String createResetToken(String email) {
        var token = UUID.randomUUID().toString();
        this.resetTokens.put(token, email);
        return token;
    }

    public Optional<User> confirmReset(String token, String newPassword) {
        var email = this.resetTokens.remove(token);
        if (email == null) {
            return Optional.empty();
        }
        var user = this.usersByEmail.get(email);
        if (user == null) {
            return Optional.empty();
        }
        var salt = this.passwordService.generateSalt();
        var hash = this.passwordService.hash(newPassword, salt);
        var updated = new User(user.id(), user.email(), user.name(), hash, salt, user.role(), user.createdAt());
        this.usersByEmail.put(email, updated);
        return Optional.of(updated);
    }
}
