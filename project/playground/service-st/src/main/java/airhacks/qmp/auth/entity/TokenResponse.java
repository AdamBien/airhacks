package airhacks.qmp.auth.entity;

public record TokenResponse(String token, long expiresIn, String refreshToken) {
}
