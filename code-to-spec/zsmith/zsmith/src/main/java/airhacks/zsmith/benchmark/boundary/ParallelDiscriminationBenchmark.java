package airhacks.zsmith.benchmark.boundary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import airhacks.zsmith.benchmark.control.LookupTool;
import airhacks.zsmith.benchmark.control.Lookups;
import airhacks.zsmith.benchmark.entity.EfficiencyResult;
import airhacks.zsmith.tools.control.ToolHandler;

/**
 * Parallel-discrimination benchmark for agents. Generates {@code tasks} independent
 * {@code id -> value} pairs and lists every id up front, so there is no data dependency: an agent
 * that recognizes independence issues all {@code lookup} calls in one turn, while one that
 * needlessly serializes spreads them across {@code tasks} turns. The metric is efficiency
 * (calls vs turns), not the secret-match of pointer chasing — this is the inverse axis.
 *
 * <p>Compose it into an {@code Agent} and score the reply:
 * <pre>{@code
 * var benchmark = new ParallelDiscriminationBenchmark(8);
 * var agent = new Agent("parallel-worker", benchmark.systemPrompt())
 *         .withTool(benchmark.tool())
 *         .withMaxIterations(28);
 * var result = benchmark.score(agent.chat("go"));
 * IO.println(result.summary());
 * }</pre>
 */
public class ParallelDiscriminationBenchmark {

    static final long DEFAULT_SEED = 0xC0FFEEL;

    final Map<String, String> values;
    final LookupTool tool;

    public ParallelDiscriminationBenchmark(int tasks) {
        this(tasks, DEFAULT_SEED);
    }

    public ParallelDiscriminationBenchmark(int tasks, long seed) {
        this.values = Lookups.random(tasks, seed);
        this.tool = new LookupTool(this.values);
    }

    public ToolHandler tool() {
        return this.tool;
    }

    public String systemPrompt() {
        var ids = String.join(", ", this.values.keySet());
        return """
               You can look up the value of an id with the lookup tool, which returns value=<token>.

               I need the values for these %d ids:
               %s

               When you have all of them, reply with ONLY the values separated by spaces,
               in any order: no ids, no labels, no extra words.
               """.formatted(this.values.size(), ids);
    }

    public EfficiencyResult score(String agentAnswer) {
        var expected = this.values.values().stream().sorted().toList();
        var actual = parse(agentAnswer);
        var correct = actual.equals(expected);
        return new EfficiencyResult(this.values.size(), this.tool.calls(),
                this.tool.turns(), this.tool.maxConcurrency(), correct);
    }

    static List<String> parse(String agentAnswer) {
        if (agentAnswer == null || agentAnswer.isBlank()) {
            return List.of();
        }
        return Arrays.stream(agentAnswer.trim().split("[\\s,]+"))
                .filter(token -> !token.isBlank())
                .sorted()
                .toList();
    }
}
