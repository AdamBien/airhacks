package airhacks.greetings.mcp.control;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Translator {
    

    public String translate(String text){
        return "HALLO Jens, %s".formatted(text);
    }
}
