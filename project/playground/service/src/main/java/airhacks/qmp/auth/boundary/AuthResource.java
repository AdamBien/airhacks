package airhacks.qmp.auth.boundary;

import airhacks.qmp.auth.control.TokenService;
import airhacks.qmp.auth.control.UserService;
import airhacks.qmp.auth.entity.Credentials;
import airhacks.qmp.auth.entity.PasswordResetConfirm;
import airhacks.qmp.auth.entity.PasswordResetRequest;
import airhacks.qmp.auth.entity.RegistrationRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("auth")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("registration")
    public Response register(RegistrationRequest request) {
        return this.userService.register(request)
                .map(user -> Response.status(Response.Status.CREATED)
                        .entity(this.tokenService.generateTokens(user))
                        .build())
                .orElse(Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"Registration failed\"}")
                        .build());
    }

    @POST
    @Path("login")
    public Response login(Credentials credentials) {
        return this.userService.authenticate(credentials.email(), credentials.password())
                .map(user -> Response.ok(this.tokenService.generateTokens(user)).build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\":\"Invalid credentials\"}")
                        .build());
    }

    @POST
    @Path("token/refresh")
    public Response refresh(@HeaderParam("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\"Invalid token\"}")
                    .build();
        }
        var email = this.jwt.getName();
        return this.userService.findByEmail(email)
                .map(user -> Response.ok(this.tokenService.generateTokens(user)).build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\":\"Invalid token\"}")
                        .build());
    }

    @POST
    @Path("password/reset")
    public Response requestReset(PasswordResetRequest request) {
        this.userService.findByEmail(request.email())
                .ifPresent(user -> this.userService.createResetToken(request.email()));
        return Response.ok("{\"message\":\"If the email exists, a reset link has been sent\"}").build();
    }

    @POST
    @Path("password/confirm")
    public Response confirmReset(PasswordResetConfirm request) {
        return this.userService.confirmReset(request.token(), request.newPassword())
                .map(user -> Response.ok("{\"message\":\"Password reset successful\"}").build())
                .orElse(Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"Invalid reset token\"}")
                        .build());
    }
}
