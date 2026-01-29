package airhacks.qmp.owners.boundary;

import airhacks.qmp.owners.entity.Owner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("owners")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OwnersResource {

    @GET
    public JsonObject owners() {
        var emptyArray = Json.createArrayBuilder().build();
        return Json.createObjectBuilder()
                .add("owners", emptyArray)
                .build();
    }

    @POST
    public Response create(JsonObject input) {
        var owner = Owner.fromJSON(input);
        return Response.status(Response.Status.CREATED)
                .entity(owner.toJSON())
                .build();
    }
}
