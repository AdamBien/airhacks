package airhacks.llm.greetings.boundary;

import org.junit.jupiter.api.Test;

class GreeterTest {

    @Test
    void greet() {
        var message = Greeter.greet();
        System.out.println(message);
    }

    @Test
    void capitalForCountry() {
        var message = Greeter.capital();
        System.out.println(message);
    }
}
