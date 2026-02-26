package airhacks.qmp.auth.entity;

public record RegistrationRequest(String email, String password, String name, Role role) {
}
