package airhacks.zsmith.benchmark.entity;

import java.util.Map;

/**
 * A hidden, single-linked chain of {@link Hop}s. Starting at {@code start}, each key
 * resolves to a hop whose {@code next} points to the following key until {@link Hop#END}.
 * Concatenating every fragment in traversal order reproduces {@code secret} — the
 * ground truth the agent must reconstruct by calling the tool {@code depth} times.
 */
public record Chain(String start, Map<String, Hop> hops, String secret) {

    public int depth() {
        return this.hops.size();
    }

    public Hop hop(String key) {
        return this.hops.get(key);
    }
}
