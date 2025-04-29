package airhacks;

import java.util.Map;

import airhacks.functionurl.boundary.ServerlessApp;

public interface CDKApp {

    static void main(String... args) {

        new ServerlessApp("valantic-greetings")
                .functionName("valantic_CoffeeBeans")
                .functionZip("../coffee/target/function.zip")
                .configuration(Map.of("GREETINGS_MESSAGE", "hello from valantic clouds!"))
                .build();
    }
}
