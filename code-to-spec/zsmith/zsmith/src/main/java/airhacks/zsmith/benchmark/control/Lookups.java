package airhacks.zsmith.benchmark.control;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Generates reproducible, independent {@code id -> value} pairs for the parallel-discrimination
 * benchmark. Ids are unguessable (so values cannot be predicted without a tool call); the pairs
 * carry no ordering or dependency, so all lookups can be issued at once.
 */
public interface Lookups {

    static Map<String, String> random(int tasks, long seed) {
        var random = new Random(seed);
        var pairs = new LinkedHashMap<String, String>();
        while (pairs.size() < tasks) {
            pairs.put(Chains.id(random), "%02d".formatted(random.nextInt(100)));
        }
        return pairs;
    }
}
