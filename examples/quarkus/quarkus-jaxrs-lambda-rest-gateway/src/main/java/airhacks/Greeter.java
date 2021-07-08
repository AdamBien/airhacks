package airhacks;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Greeter {
    
    public String helloFromClouds() {
        return "clouds are great, sun is better";
    }
}
