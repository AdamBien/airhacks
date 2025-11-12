package airhacks;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @PostConstruct
    public void init(){
        System.out.println(this.getClass() + " init");
    }

    @PreDestroy
    public void destroy(){
        System.out.println(this.getClass() + " destroy");
    }


    @GET
    public String hello() {
        System.out.println("- " + this.tweeter.getClass());
        return this.tweeter.greet();
    }

    @GET
    @Path("{id}-{name}")
    public String byId(@PathParam("id") String id,@PathParam("name") String name) {
        System.out.println("- " +  id + " " + name);
        return this.tweeter.greet() + " - " +  id + " " + name;
    }

    @POST
    public Response hello(String message) {
        System.out.println("- " + message);
        return Response.noContent().build();
    }
}
