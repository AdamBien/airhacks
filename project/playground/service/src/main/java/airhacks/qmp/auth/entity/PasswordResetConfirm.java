package airhacks.qmp.auth.entity;

public record PasswordResetConfirm(String token, String newPassword) {
}
