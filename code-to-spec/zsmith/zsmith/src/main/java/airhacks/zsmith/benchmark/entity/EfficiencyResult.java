package airhacks.zsmith.benchmark.entity;

import java.util.Locale;

/**
 * Outcome of a parallel-discrimination run. {@code correct} gates the result (all values
 * retrieved); {@code turns} vs {@code calls} is the headline signal: with {@code tasks}
 * independent calls, a batching agent collapses them into few turns (high {@code maxConcurrency}),
 * a serializing one spreads them across {@code tasks} turns ({@code maxConcurrency == 1}).
 */
public record EfficiencyResult(int tasks, int calls, int turns, int maxConcurrency, boolean correct) {

    public double efficiency() {
        return this.turns == 0 ? 0 : (double) this.calls / this.turns;
    }

    public String summary() {
        return String.format(Locale.ROOT,
                "pd tasks=%d calls=%d turns=%d maxConcurrency=%d efficiency=%.1f correct=%s",
                this.tasks, this.calls, this.turns, this.maxConcurrency,
                efficiency(), this.correct ? "yes" : "no");
    }
}
