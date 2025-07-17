package airhacks.velocity.greeter.boundary;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import java.io.StringWriter;
import java.util.stream.IntStream;

public class Greeter {

    VelocityEngine engine;

    public Greeter() {
        this.engine = new VelocityEngine();
        this.engine.init();
    }

    public String greet(String name) {
        var context = new VelocityContext();
        context.put("NAME", name);

        var template = "Hello, $NAME!";

        var writer = new StringWriter();
        engine.evaluate(context, writer, "greeting", template);

        return writer.toString();
    }

    public String greet(String name, int counter) {
        var numbers = IntStream.rangeClosed(1, counter)
                .boxed()
                .toList();
        var context = new VelocityContext();
        context.put("NAME", name);
        context.put("numbers", numbers);

        var template = """
                #foreach($number in $numbers)
                $number. Hello, $NAME!
                #end""";

        var writer = new StringWriter();
        engine.evaluate(context, writer, "greeting-with-counter", template);

        return writer.toString();
    }
}