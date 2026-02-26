package airhacks.qmp.auth.boundary;

import airhacks.qmp.auth.entity.Credentials;
import airhacks.qmp.auth.entity.PasswordResetConfirm;
import airhacks.qmp.auth.entity.PasswordResetRequest;
import airhacks.qmp.auth.entity.RegistrationRequest;
import airhacks.qmp.auth.entity.Role;
import airhacks.qmp.auth.entity.TokenResponse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthResourceIT {

    @Inject
    @RestClient
    AuthResourceClient client;

    static String customerToken;
    static String customerRefreshToken;

    @Test
    @Order(1)
    void registerCustomer() {
        var request = new RegistrationRequest("customer@test.com", "secret123", "Test Customer", Role.CUSTOMER);
        var response = this.client.register(request);
        assertThat(response.getStatus()).isEqualTo(201);
        var tokenResponse = response.readEntity(TokenResponse.class);
        assertThat(tokenResponse.token()).isNotNull();
        assertThat(tokenResponse.refreshToken()).isNotNull();
        assertThat(tokenResponse.expiresIn()).isGreaterThan(0);
        customerToken = tokenResponse.token();
        customerRefreshToken = tokenResponse.refreshToken();
    }

    @Test
    @Order(2)
    void registerSeller() {
        var request = new RegistrationRequest("seller@test.com", "secret123", "Test Seller", Role.SELLER);
        var response = this.client.register(request);
        assertThat(response.getStatus()).isEqualTo(201);
        var tokenResponse = response.readEntity(TokenResponse.class);
        assertThat(tokenResponse.token()).isNotNull();
    }

    @Test
    @Order(3)
    void registerDuplicateEmail() {
        var request = new RegistrationRequest("customer@test.com", "secret123", "Duplicate", Role.CUSTOMER);
        var response = this.client.register(request);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @Order(4)
    void loginValid() {
        var credentials = new Credentials("customer@test.com", "secret123");
        var response = this.client.login(credentials);
        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = response.readEntity(TokenResponse.class);
        assertThat(tokenResponse.token()).isNotNull();
        customerToken = tokenResponse.token();
    }

    @Test
    @Order(5)
    void loginInvalidPassword() {
        var credentials = new Credentials("customer@test.com", "wrongpassword");
        var response = this.client.login(credentials);
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @Order(6)
    void loginNonexistentEmail() {
        var credentials = new Credentials("nobody@test.com", "secret123");
        var response = this.client.login(credentials);
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @Order(7)
    void tokenRefresh() {
        var response = this.client.refresh("Bearer " + customerToken);
        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = response.readEntity(TokenResponse.class);
        assertThat(tokenResponse.token()).isNotNull();
    }

    @Test
    @Order(8)
    void passwordResetAlways200() {
        var response = this.client.requestReset(new PasswordResetRequest("customer@test.com"));
        assertThat(response.getStatus()).isEqualTo(200);

        var responseNonExistent = this.client.requestReset(new PasswordResetRequest("nobody@test.com"));
        assertThat(responseNonExistent.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(9)
    void invalidResetToken() {
        var response = this.client.confirmReset(new PasswordResetConfirm("invalid-token", "newpassword"));
        assertThat(response.getStatus()).isEqualTo(400);
    }
}
