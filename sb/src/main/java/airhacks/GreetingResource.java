package airhacks;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/hello")
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
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        System.out.println("- " + this.tweeter.getClass());
        return this.tweeter.greet();
    }
}
