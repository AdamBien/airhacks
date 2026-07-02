package airhacks.qmp.attendees.boundary;

import java.net.URI;

import airhacks.qmp.attendees.control.Attendees;
import airhacks.qmp.attendees.entity.Attendee;
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

@Path("attendees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendeesResource {

    @Inject
    Attendees attendees;

    @GET
    public Response all() {
        var all = this.attendees.all().stream()
                .map(Attendee::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(all).build();
    }

    @GET
    @Path("{id}")
    public Response byId(@PathParam("id") String id) {
        var attendee = this.attendees.byId(id)
                .orElseThrow(NotFoundException::new);
        return Response.ok(attendee.toJSON()).build();
    }

    @POST
    public Response register(JsonObject json) {
        var registered = this.attendees.register(Attendee.fromJSON(json));
        return Response.created(URI.create("attendees/" + registered.id()))
                .entity(registered.toJSON())
                .build();
    }
}
