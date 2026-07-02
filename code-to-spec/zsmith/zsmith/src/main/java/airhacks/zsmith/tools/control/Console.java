package airhacks.zsmith.tools.control;

import airhacks.zsmith.logging.control.Log;

public class Console {

    public static String prompt(String message) {
        Log.user(message);
        return System.console().readLine().trim();
    }
}
