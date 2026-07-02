package airhacks.zsmith.benchmark.entity;

/**
 * A single hop: the {@code fragment} contributed to the secret and the
 * {@code next} key to follow. A {@code next} of {@link #END} marks the end of the chain.
 */
public record Hop(String fragment, String next) {

    public static final String END = "END";

    public boolean terminal() {
        return END.equals(this.next);
    }
}
