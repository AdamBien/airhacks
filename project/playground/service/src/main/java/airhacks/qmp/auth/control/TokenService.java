package airhacks.qmp.auth.control;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import airhacks.qmp.auth.entity.TokenResponse;
import airhacks.qmp.auth.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TokenService {

    @Inject
    @ConfigProperty(name = "auth.token.expiration.minutes", defaultValue = "30")
    long tokenExpirationMinutes;

    @Inject
    @ConfigProperty(name = "auth.refresh.token.expiration.minutes", defaultValue = "10080")
    long refreshTokenExpirationMinutes;

    public TokenResponse generateTokens(User user) {
        var token = Jwt.issuer("airzon-store")
                .upn(user.email())
                .subject(user.id())
                .groups(Set.of(user.role().name()))
                .claim("name", user.name())
                .expiresIn(Duration.ofMinutes(this.tokenExpirationMinutes))
                .sign();

        var refreshToken = Jwt.issuer("airzon-store")
                .upn(user.email())
                .subject(user.id())
                .claim("type", "refresh")
                .expiresIn(Duration.ofMinutes(this.refreshTokenExpirationMinutes))
                .jws().keyId(UUID.randomUUID().toString())
                .sign();

        return new TokenResponse(token, this.tokenExpirationMinutes * 60, refreshToken);
    }
}
