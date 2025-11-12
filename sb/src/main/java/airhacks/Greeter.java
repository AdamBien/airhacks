package airhacks;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Greeter {

    @Inject
    @ConfigProperty(name="first.name",defaultValue = "james")
    String firstName;
    
    public String greet(){
        System.out.println("hello, duke");
        return "hello, %s".formatted(this.firstName);
    }
}
