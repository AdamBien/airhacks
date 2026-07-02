package airhacks.zsmith.benchmark.control;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import airhacks.zsmith.tools.control.ToolHandler;

/**
 * Independent-lookup tool for the parallel-discrimination benchmark. Resolves one {@code id} per
 * call and, because {@link #parallel()} is {@code true}, calls the agent batches into a single
 * turn run concurrently on the Agent's virtual-thread executor and therefore overlap in time.
 *
 * <p>That overlap is the measurement: a concurrency gauge records {@code maxConcurrency} (the most
 * calls in flight at once) and {@code turns} (the number of {@code 0 -> in flight} waves — one per
 * agent turn that issued calls). A batching agent yields few turns and high concurrency; a
 * serializing one yields one turn per call and concurrency 1. The short sleep guarantees same-turn
 * calls overlap so the gauge is reliable; virtual threads make the wait free.
 */
public class LookupTool implements ToolHandler {

    public enum Field { id }

    static final String NAME = "lookup";
    static final long OVERLAP_MILLIS = 20;

    final Map<String, String> values;
    final AtomicInteger calls;
    final Object gauge;
    int inFlight;
    int maxConcurrency;
    int turns;

    public LookupTool(Map<String, String> values) {
        this.values = values;
        this.calls = new AtomicInteger();
        this.gauge = new Object();
    }

    public int calls() {
        return this.calls.get();
    }

    public int turns() {
        synchronized (this.gauge) {
            return this.turns;
        }
    }

    public int maxConcurrency() {
        synchronized (this.gauge) {
            return this.maxConcurrency;
        }
    }

    @Override
    public boolean parallel() {
        return true;
    }

    @Override
    public String toolName() {
        return NAME;
    }

    @Override
    public String description() {
        return "Returns the value for an id as 'value=<token>'. Each id is independent of the others.";
    }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(ToolHandler.Prop.string(Field.id, "The id to look up"));
    }

    @Override
    public String execute(JSONObject input) {
        this.calls.incrementAndGet();
        enter();
        try {
            Thread.sleep(OVERLAP_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            leave();
        }
        var id = input.getString(Field.id.name());
        var value = this.values.get(id);
        if (value == null) {
            return "ERROR: unknown id '" + id + "'.";
        }
        return "value=" + value;
    }

    void enter() {
        synchronized (this.gauge) {
            if (this.inFlight == 0) {
                this.turns++;
            }
            this.inFlight++;
            this.maxConcurrency = Math.max(this.maxConcurrency, this.inFlight);
        }
    }

    void leave() {
        synchronized (this.gauge) {
            this.inFlight--;
        }
    }
}
