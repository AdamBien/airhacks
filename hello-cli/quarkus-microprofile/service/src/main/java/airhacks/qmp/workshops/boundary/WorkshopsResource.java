package airhacks.qmp.workshops.boundary;

import java.net.URI;

import airhacks.qmp.workshops.control.Workshops;
import airhacks.qmp.workshops.entity.Workshop;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("workshops")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkshopsResource {

    @Inject
    Workshops workshops;

    @GET
    public Response all() {
        var all = this.workshops.all().stream()
                .map(Workshop::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(all).build();
    }

    @GET
    @Path("{id}")
    public Response byId(@PathParam("id") String id) {
        var workshop = this.workshops.byId(id)
                .orElseThrow(NotFoundException::new);
        return Response.ok(workshop.toJSON()).build();
    }

    @POST
    public Response schedule(JsonObject json) {
        var scheduled = this.workshops.schedule(Workshop.fromJSON(json));
        return Response.created(URI.create("workshops/" + scheduled.id()))
                .entity(scheduled.toJSON())
                .build();
    }
}
