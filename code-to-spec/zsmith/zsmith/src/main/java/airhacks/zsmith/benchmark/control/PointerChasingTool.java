package airhacks.zsmith.benchmark.control;

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import airhacks.zsmith.benchmark.entity.Chain;
import airhacks.zsmith.tools.control.ToolHandler;

/**
 * Dedicated benchmark tool: resolves one hop per call and counts its own invocations.
 * Each call returns {@code fragment=<value> next=<key>}; the input of every call is the
 * output of the previous one, forcing strictly sequential pointer chasing.
 */
public class PointerChasingTool implements ToolHandler {

    public enum Field { key }

    static final String NAME = "follow_pointer";

    final Chain chain;
    final AtomicInteger calls;

    public PointerChasingTool(Chain chain) {
        this.chain = chain;
        this.calls = new AtomicInteger();
    }

    public int calls() {
        return this.calls.get();
    }

    @Override
    public String toolName() {
        return NAME;
    }

    @Override
    public String description() {
        return "Returns the next hop for a key as 'fragment=<value> next=<key>'. "
                + "When next=END the chain is complete.";
    }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(ToolHandler.Prop.string(Field.key, "The current key in the chain"));
    }

    @Override
    public String execute(JSONObject input) {
        this.calls.incrementAndGet();
        var key = input.getString(Field.key.name());
        var hop = this.chain.hop(key);
        if (hop == null) {
            return "ERROR: unknown key '" + key + "'. Re-check the previous next value.";
        }
        return "fragment=" + hop.fragment() + " next=" + hop.next();
    }
}
