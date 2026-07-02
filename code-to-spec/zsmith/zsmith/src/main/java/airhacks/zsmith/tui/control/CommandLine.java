package airhacks.zsmith.tui.control;

import java.util.Arrays;

public interface CommandLine {

    static int firstInt(String[] args, int fallback) {
        return Arrays.stream(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(fallback);
    }

    static int port(String[] args, int fallback) {
        return firstInt(args, fallback);
    }
}
