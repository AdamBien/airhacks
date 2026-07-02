package airhacks.qmp.sessions.boundary;

import java.net.URI;

import airhacks.qmp.sessions.control.Sessions;
import airhacks.qmp.sessions.entity.Session;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionsResource {

    @Inject
    Sessions sessions;

    @GET
    public Response all() {
        var all = this.sessions.all().stream()
                .map(Session::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(all).build();
    }

    @GET
    @Path("{id}")
    public Response byId(@PathParam("id") String id) {
        var session = this.sessions.byId(id)
                .orElseThrow(NotFoundException::new);
        return Response.ok(session.toJSON()).build();
    }

    @GET
    @Path("byWorkshop")
    public Response byWorkshop(@QueryParam("workshopId") String workshopId) {
        var workshopSessions = this.sessions.byWorkshop(workshopId).stream()
                .map(Session::toJSON)
                .collect(JsonCollectors.toJsonArray());
        return Response.ok(workshopSessions).build();
    }

    @POST
    public Response add(JsonObject json) {
        var added = this.sessions.add(Session.fromJSON(json));
        return Response.created(URI.create("sessions/" + added.id()))
                .entity(added.toJSON())
                .build();
    }
}
