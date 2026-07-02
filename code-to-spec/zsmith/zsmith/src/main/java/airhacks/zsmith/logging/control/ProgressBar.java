package airhacks.zsmith.logging.control;

public class ProgressBar {

    static final int BAR_WIDTH = 20;
    static final String FILLED = "█";
    static final String EMPTY = "░";
    static final String RESET = "\u001B[0m";

    private final int maxIterations;
    private int llmInvocations;
    private int toolInvocations;

    public ProgressBar(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void addLLMInvocation() {
        this.llmInvocations++;
    }

    public void addToolInvocations(int count) {
        this.toolInvocations += count;
    }

    public void update(int iteration) {
        System.out.println(render(iteration));
    }

    String render(int iteration) {
        int filled = (int) ((double) iteration / maxIterations * BAR_WIDTH);
        int empty = BAR_WIDTH - filled;

        var blue = Log.Color.BLUE.code;
        var violet = Log.Color.VIOLET.code;
        var cyan = Log.Color.CYAN.code;
        var magenta = Log.Color.MAGENTA.code;

        return blue + "[" + FILLED.repeat(filled) + RESET
                + violet + EMPTY.repeat(empty) + RESET
                + blue + "]" + RESET
                + "  " + iteration + "/" + maxIterations
                + "  " + cyan + "llm: " + llmInvocations + RESET
                + "  " + magenta + "tools: " + toolInvocations + RESET;
    }

    public void summary() {
        var cyan = Log.Color.CYAN.code;
        var magenta = Log.Color.MAGENTA.code;
        var green = Log.Color.GREEN.code;
        System.out.println(green + "--- summary ---" + RESET
                + "  " + cyan + "llm: " + llmInvocations + RESET
                + "  " + magenta + "tools: " + toolInvocations + RESET);
    }
}
