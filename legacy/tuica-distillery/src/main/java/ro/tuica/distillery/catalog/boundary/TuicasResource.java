package ro.tuica.distillery.catalog.boundary;

import java.net.URI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import ro.tuica.distillery.catalog.entity.Tuica;

@ApplicationScoped
@Path("tuicas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TuicasResource {

    static final System.Logger LOGGER = System.getLogger(TuicasResource.class.getName());

    @Inject
    TuicaStore store;

    @GET
    public Response all() {
        var builder = Json.createArrayBuilder();
        store.all().stream()
                .map(Tuica::toJSON)
                .forEach(builder::add);
        var array = builder.build();
        return Response.ok(array).build();
    }

    @GET
    @Path("{name}")
    public Response get(@PathParam("name") String name) {
        return Response.ok(new Tuica(name, 42).toJSON()).build();
    }

    @POST
    public Response store(JsonObject tuica, @Context UriInfo info) {
        LOGGER.log(System.Logger.Level.INFO, "--- {0}", tuica);
        store.store(Tuica.fromJSON(tuica));
        URI uri = info.getAbsolutePathBuilder().path("/" + tuica.getString("name")).build();
        return Response.created(uri).header("x-details", "great stuff").build();
    }
}
