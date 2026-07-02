package airhacks.zsmith.logging.control;

import java.io.PrintStream;

import airhacks.zsmith.configuration.control.ZCfg;

public enum Log {

    ERROR("❌", Color.RED, System.err),
    WARNING("⚠️", Color.YELLOW, System.out),
    INFO("ℹ️", Color.GREEN, System.out),
    DEBUG("🔍", Color.BASE01, System.out),
    AGENT("🤖", Color.BLUE, System.out),
    PROMPT("💬", Color.VIOLET, System.out),
    ANSWER("💡", Color.CYAN, System.out),
    TOOL("🔧", Color.MAGENTA, System.out),
    SKILL("⚡", Color.ORANGE, System.out),
    USER("👤", Color.BASE1, System.out),
    SUBAGENT("🔀", Color.BASE1, System.out),
    MEMORY("🧠", Color.BASE0, System.out),
    REQUEST("📤", Color.BASE00, System.out, "log.request"),
    RESPONSE("📥", Color.BASE00, System.out, "log.response"),
    LLM("🧩", Color.BASE01, System.out, "log.llm"),
    TOKENS("🪙", Color.BASE0, System.out);

    private PrintStream out;

    enum Color {
        // Accents
        YELLOW("\033[38;2;181;137;0m"),       // Solarized Yellow #b58900
        ORANGE("\033[38;2;203;75;22m"),       // Solarized Orange #cb4b16
        RED("\033[38;2;220;50;47m"),          // Solarized Red #dc322f
        MAGENTA("\033[38;2;211;54;130m"),     // Solarized Magenta #d33682
        VIOLET("\033[38;2;108;113;196m"),     // Solarized Violet #6c71c4
        BLUE("\033[38;2;38;139;210m"),        // Solarized Blue #268bd2
        CYAN("\033[38;2;42;161;152m"),        // Solarized Cyan #2aa198
        GREEN("\033[38;2;133;153;0m"),        // Solarized Green #859900
        // Content greys — the foreground-safe Solarized base tones
        BASE01("\033[38;2;88;110;117m"),      // Solarized Base01 #586e75
        BASE00("\033[38;2;101;123;131m"),     // Solarized Base00 #657b83
        BASE0("\033[38;2;131;148;150m"),      // Solarized Base0 #839496
        BASE1("\033[38;2;147;161;161m");      // Solarized Base1 #93a1a1

        String code;

        Color(String code) {
            this.code = code;
        }
    }

    private final String emoji;
    private final String value;
    private final String configKey;
    private final static String RESET = "\u001B[0m";

    private Log(String emoji, Color color, PrintStream out) {
        this(emoji, color, out, null);
    }

    private Log(String emoji, Color color, PrintStream out, String configKey) {
        this.emoji = emoji;
        this.value = (color.code + "%s" + RESET);
        this.out = out;
        this.configKey = configKey;
    }

    String formatted(String raw) {
        return this.emoji + " " + this.value.formatted(raw);
    }

    void out(String message) {
        if (configKey != null && !ZCfg.bool(configKey, false)) return;
        var colored = formatted(message);
        this.out.println(colored);
    }

    void outInline(String message) {
        if (configKey != null && !ZCfg.bool(configKey, false)) return;
        var colored = formatted(message);
        this.out.print(colored);
    }

    void outEndline(String message) {
        if (configKey != null && !ZCfg.bool(configKey, false)) return;
        var colored = formatted(message);
        this.out.println(" " + colored);
    }

    public static void error(String message) {
        Log.ERROR.out(message);
    }

    public static void error(String message, Exception e) {
        Log.ERROR.out(message + ": " + e.getMessage());
        e.printStackTrace(System.err);
    }

    public static void warning(String message) {
        Log.WARNING.out(message);
    }

    public static void info(String message) {
        Log.INFO.out(message);
    }

    public static void debug(String message) {
        Log.DEBUG.out(message);
    }

    public static void agent(String message) {
        Log.AGENT.out(message);
    }

    public static void prompt(String message) {
        Log.PROMPT.out(message);
    }

    public static void answer(String message) {
        Log.ANSWER.out(message);
    }

    public static void tool(String message) {
        Log.TOOL.out(message);
    }

    public static void toolStart(String message) {
        Log.TOOL.outEndline(message);
    }

    public static void toolEnd(String message) {
        Log.TOOL.outEndline(message);
    }

    public static void skill(String message) {
        Log.SKILL.out(message);
    }

    public static void user(String message) {
        Log.USER.out(message);
    }

    public static void subagent(String message) {
        Log.SUBAGENT.out(message);
    }

    public static void memory(String message) {
        Log.MEMORY.out(message);
    }

    public static void request(String message) {
        Log.REQUEST.out(message);
    }

    public static void response(String message) {
        Log.RESPONSE.out(message);
    }

    public static void llm(String message) {
        Log.LLM.out(message);
    }

    public static void tokens(String message) {
        Log.TOKENS.out(message);
    }

    public static void clearScreen() {
        System.out.println("\033c");
    }

    public static void stop(String message) {
        error(message);
        System.exit(0);
    }
}
