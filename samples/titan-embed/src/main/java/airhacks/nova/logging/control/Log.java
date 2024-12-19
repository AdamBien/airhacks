package airhacks.nova.logging.control;

import java.io.PrintStream;

import airhacks.nova.amazon.boundary.Embeddings;

public enum Log {

    ERROR(Color.RED, System.err),
    PROMPT(Color.PROMPT, System.out),
    INFO(Color.INFO, System.out),
    SYSTEM(Color.DARKBLUE, System.out),
    ANSWER(Color.ANSWER, System.out),
    DEBUG(Color.BLACK_ON_WHITE, System.out);

    PrintStream out;

    enum Color {
        PROMPT("\033[0;90m"),
        INFO("\033[0;33m"),
        DARKBLUE("\033[2;34m"),
        ANSWER("\033[0;97m"),
        RED("\033[0;41m"),
        GREEN("\033[0;42m"),
        YELLOW("\033[0;43m"),
        BLUE("\033[0;44m"),
        PURPLE("\033[0;45m"),
        CYAN("\033[0;46m"),
        WHITE("\033[0;47m"),
        BLACK_ON_WHITE("\u001B[30;107m");

        String code;

        Color(String code) {
            this.code = code;
        }
    }

    private final String value;
    private final static String RESET = "\u001B[0m";

    private Log(Color color, PrintStream out) {
        this.value = (color.code + "%s" + RESET);
        this.out = out;
    }

    public String formatted(String raw) {
        return this.value.formatted(raw);
    }

    public void out(String message) {
        var colored = formatted(message);
        this.out.println(colored);
    }

    public static void debug(String message) {
        if (Embeddings.debug)
            Log.DEBUG.out(message);
    }

    public static void error(String message) {
        Log.ERROR.out(message);
    }

    public static void user(String message) {
        Log.INFO.out(message);
    }

}
