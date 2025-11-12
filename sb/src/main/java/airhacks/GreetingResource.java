package airhacks;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("greetings")
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource {

    @Inject
    Greeter tweeter;

    @Inject
    User user;

    public GreetingResource(){
        System.out.println(this.getClass() + " ctr " + this.tweeter);

    }

    @PostConstruct
    public void init() {
        System.out.println(this.getClass() + " init " + this.tweeter);
    }

    @PreDestroy
    public void destroy() {
        System.out.println(this.getClass() + " destroy");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject hello() {
        this.user.setName("duke " + System.currentTimeMillis());
        System.out.println("- " + this.tweeter.getClass());
        var greet = this.tweeter.greet();

        return Json.createObjectBuilder()
                .add("greet", greet)
                .build();
    }

    @GET
    @Path("{id}-{name}")
    public String byId(@PathParam("id") String id, @PathParam("name") String name) {
        System.out.println("- " + id + " " + name);
        return this.tweeter.greet() + " - " + id + " " + name;
    }

    @POST
    public Response hello(String message) {
        System.out.println("- " + message);
        return Response.noContent().build();
    }
}
