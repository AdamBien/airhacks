package airhacks.zsmith.benchmark.boundary;

import airhacks.zsmith.benchmark.control.Chains;
import airhacks.zsmith.benchmark.control.PointerChasingTool;
import airhacks.zsmith.benchmark.entity.BenchmarkResult;
import airhacks.zsmith.benchmark.entity.Chain;
import airhacks.zsmith.tools.control.ToolHandler;

/**
 * Pointer-chasing benchmark for agents. A hidden chain of {@code depth} hops is generated;
 * the agent must follow it hop by hop via {@link #tool()} and reconstruct the secret. Because
 * every hop's key is read from the previous tool result, the agent cannot shortcut — it must
 * complete a {@code depth}-long sequential tool-calling loop.
 *
 * <p>Compose it into an {@code Agent} and score the reply:
 * <pre>{@code
 * var benchmark = new PointerChasingBenchmark(50);
 * var agent = new Agent("pointer-chaser", benchmark.systemPrompt())
 *         .withTool(benchmark.tool())
 *         .withMaxIterations(70);
 * var result = benchmark.score(agent.chat("go"));
 * IO.println(result.summary());
 * }</pre>
 */
public class PointerChasingBenchmark {

    static final long DEFAULT_SEED = 0xC0FFEEL;

    final Chain chain;
    final PointerChasingTool tool;

    public PointerChasingBenchmark(int depth) {
        this(depth, DEFAULT_SEED);
    }

    public PointerChasingBenchmark(int depth, long seed) {
        this.chain = Chains.random(depth, seed);
        this.tool = new PointerChasingTool(this.chain);
    }

    public ToolHandler tool() {
        return this.tool;
    }

    public String startKey() {
        return this.chain.start();
    }

    public String systemPrompt() {
        return """
               You are chasing a chain of pointers to reconstruct a hidden secret.

               Start at key "%s".
               Repeat these steps:
                 1. Call follow_pointer with the current key.
                 2. The tool returns: fragment=<value> next=<key>.
                 3. Append <value> to your collected secret, preserving order.
                 4. If next is END, stop. Otherwise set the current key to <key> and repeat.

               When finished, reply with ONLY the concatenated fragments in the exact
               order you collected them: no spaces, no separators, no extra words.
               """.formatted(this.chain.start());
    }

    public BenchmarkResult score(String agentAnswer) {
        var actual = agentAnswer == null ? "" : agentAnswer.replaceAll("\\s", "");
        var passed = this.chain.secret().equals(actual);
        return new BenchmarkResult(this.chain.depth(), this.tool.calls(), passed, this.chain.secret(), actual);
    }
}
