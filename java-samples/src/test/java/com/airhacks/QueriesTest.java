package com.airhacks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class QueriesTest {

    private ScriptEngine scriptEngine;

    @Before
    public void initEngine() {
        ScriptEngineManager m = new ScriptEngineManager();
        this.scriptEngine = m.getEngineByName("javascript");
    }

    @Test
    public void query() throws ScriptException {
        List<Machine> machine = new ArrayList<>();
        machine.add(new Machine("john", 2));
        machine.add(new Machine("ford", 42));
        machine.add(new Machine("claas", 2));
        machine.add(new Machine("fiat", 21));
        machine.add(new Machine("fendt", 3));

        Predicate<Machine> filter = getFlexibleFilter();

        double average = machine.parallelStream().filter(filter).
                mapToInt(m -> m.getPower()).average().orElse(-1);
        System.out.println("average = " + average);
    }

    private Predicate<Machine> getFilter() {
        return new Predicate<Machine>() {
            @Override
            public boolean test(Machine t) {
                return t.getName().startsWith("f");
            }
        };
    }

    private Predicate<Machine> getFlexibleFilter() throws ScriptException {
        Invocable invocable = (Invocable) this.scriptEngine;
        this.scriptEngine.eval("function test(m){return m.name.startsWith('j');}");
        return invocable.getInterface(Predicate.class);
    }

}
