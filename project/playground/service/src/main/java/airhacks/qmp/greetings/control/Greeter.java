package airhacks.qmp.greetings.control;

import static java.lang.System.Logger.Level.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Greeter {

    static System.Logger LOG = System.getLogger(Greeter.class.getName()); 

    @Inject
    @ConfigProperty(defaultValue = "hello, Quarkus on BCE", name="message")
    String message;
    
    public String greetings() {
        LOG.log(INFO, "returning: " + this.message);
        return this.message;
    }

    public void greetings(String userMessage) {
        LOG.log(INFO, "received: " + userMessage);
    }
}
