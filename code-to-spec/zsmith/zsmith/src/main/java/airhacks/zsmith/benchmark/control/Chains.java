package airhacks.zsmith.benchmark.control;

import java.util.LinkedHashMap;
import java.util.Random;

import airhacks.zsmith.benchmark.entity.Chain;
import airhacks.zsmith.benchmark.entity.Hop;

/**
 * Generates reproducible pointer-chasing {@link Chain}s. Keys are large random ids so the
 * agent cannot predict the next hop — it must call the tool every single step.
 */
public interface Chains {

    static Chain random(int depth, long seed) {
        var random = new Random(seed);
        var hops = new LinkedHashMap<String, Hop>();
        var secret = new StringBuilder();
        var next = Hop.END; // build the chain from its end backwards
        for (var i = 0; i < depth; i++) {
            var key = id(random);
            while (hops.containsKey(key)) { // keys must stay unique
                key = id(random);
            }
            var fragment = "%02d".formatted(random.nextInt(100));
            hops.put(key, new Hop(fragment, next));
            secret.insert(0, fragment); // prepend: fragments are generated last hop first
            next = key; // this node becomes the previous node's target
        }
        return new Chain(next, hops, secret.toString()); // the last key generated is the start
    }

    static String id(Random random) {
        return Long.toString(random.nextLong() & Long.MAX_VALUE); // unguessable 18-19 digit id
    }
}
