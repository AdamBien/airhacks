package ro.tuica.distillery.business.catalog.boundary;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import ro.tuica.distillery.business.catalog.entity.Tuica;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("tuicas")
public class TuicasResource {

    @Inject
    TuicaService ts;

    @GET
    public void all(@Suspended AsyncResponse response) {
        response.setTimeout(10, TimeUnit.SECONDS);
        JsonObject palinca = Json.createObjectBuilder().
                add("name", "palinca").
                build();
        JsonObject horinca
                = Json.createObjectBuilder().
                add("name", "horinca").
                build();
        JsonArray array = Json.createArrayBuilder().add(palinca).add(horinca).build();
        response.setTimeoutHandler((asyncResponse) -> {
            Response response1 = Response.status(204).build();
            asyncResponse.resume(response1);
        });
        response.resume(array);
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Tuica> all() {
        return this.ts.strong();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "tuica/express"})
    @Path("{name}")
    public Tuica get(@PathParam("name") String name) {
        return new Tuica(name, 42);
    }

    @POST
    public Response store(@NotNull JsonObject tuica, @Context UriInfo info) {
        System.out.println("--- " + tuica);
        ts.store(new Tuica(tuica.getString("name"), tuica.getInt("strength")));
        URI uri = info.getAbsolutePathBuilder().path("/nicehorinca").build();
        return Response.created(uri).header("x-details", "great stuff").build();
    }

}
