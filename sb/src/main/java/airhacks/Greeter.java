package airhacks;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class Greeter {

    @Inject
    @ConfigProperty(name="first.name",defaultValue = "james")
    String firstName;

    @PostConstruct
    public void init(){
        System.out.println(this.getClass() + " init");
    }

    @PreDestroy
    public void destroy(){
        System.out.println(this.getClass() + " destroy");
    }

    
    public String greet(){
        System.out.println("hello, duke");
        return "hello, %s".formatted(this.firstName);
    }
}
