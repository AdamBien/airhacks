package airhacks.qmp.swag.boundary;

import airhacks.qmp.swag.control.Claims;
import airhacks.qmp.swag.entity.Claim;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("claims")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClaimsResource {

    @Inject
    Claims claims;

    @POST
    public Response claim(Claim claim) {
        var confirmed = this.claims.submit(claim);
        return Response.status(Response.Status.CREATED).entity(confirmed).build();
    }
}
