package airhacks.zsmith.benchmark.entity;

/**
 * Outcome of a single benchmark run. {@code toolCalls} vs {@code depth} reveals whether
 * the agent walked the chain exactly (efficiency); {@code passed} reflects whether the
 * reconstructed secret matched (correctness).
 */
public record BenchmarkResult(int depth, int toolCalls, boolean passed, String expected, String actual) {

    public String summary() {
        var status = this.passed ? "PASS" : "FAIL";
        var detail = this.passed ? "" : " expected=" + this.expected + " actual=" + this.actual;
        return "%s depth=%d toolCalls=%d/%d%s".formatted(status, this.depth, this.toolCalls, this.depth, detail);
    }
}
