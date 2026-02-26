package airhacks.qmp.auth.boundary;

import airhacks.qmp.auth.entity.Credentials;
import airhacks.qmp.auth.entity.PasswordResetConfirm;
import airhacks.qmp.auth.entity.PasswordResetRequest;
import airhacks.qmp.auth.entity.RegistrationRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("auth")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthResourceClient {

    @POST
    @Path("registration")
    Response register(RegistrationRequest request);

    @POST
    @Path("login")
    Response login(Credentials credentials);

    @POST
    @Path("token/refresh")
    Response refresh(@HeaderParam("Authorization") String authorization);

    @POST
    @Path("password/reset")
    Response requestReset(PasswordResetRequest request);

    @POST
    @Path("password/confirm")
    Response confirmReset(PasswordResetConfirm request);
}
