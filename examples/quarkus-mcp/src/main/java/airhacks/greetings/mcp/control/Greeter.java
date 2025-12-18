package airhacks.greetings.mcp.control;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Greeter {
    

    public String hello(String name){
        return "hello, %s".formatted(name);
    }
}
