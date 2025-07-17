package airhacks.velocity.greeter.boundary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GreeterTest {
    
    Greeter greeter;
    
    @BeforeEach
    void setUp() {
        this.greeter = new Greeter();
    }
    
    @Test
    void greetWithName() {
        var name = "Duke";
        var greeting = greeter.greet(name);
        assertThat(greeting).isEqualTo("Hello, Duke!");
    }
    
     
    @Test
    void greetWithCounter() {
        var name = "MicroProfile";
        var greeting = greeter.greet(name, 3);
        assertThat(greeting).isEqualTo("""
            1. Hello, MicroProfile!
            2. Hello, MicroProfile!
            3. Hello, MicroProfile!
            """);
    }
    
}