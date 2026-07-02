package airhacks.zsmith.tools.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.logging.control.Log;

public interface LaunchAppTool {

    int DEFAULT_TIMEOUT_SECONDS = 30;

    enum Field { arguments }

    static ToolHandler create(String name, String description, String command) {
        return create(name, description, command, DEFAULT_TIMEOUT_SECONDS);
    }

    static ToolHandler create(String name, String description, String command, int timeoutSeconds) {
        return ToolHandler.of(
                name,
                description,
                ToolHandler.schema(ToolHandler.Prop.string(Field.arguments, "Arguments to pass to the application")),
                input -> run(input, command, timeoutSeconds));
    }

    static ToolHandler fromConfig() {
        return create(
                ZCfg.requiredString("launch.tool.name"),
                ZCfg.requiredString("launch.tool.description"),
                ZCfg.requiredString("launch.command"));
    }

    private static String run(JSONObject input, String command, int timeoutSeconds) {
        var arguments = input.optString(Field.arguments.name(), "");
        var commandLine = buildCommandLine(command, arguments);
        try {
            Log.tool("launching: " + String.join(" ", commandLine));
            var process = new ProcessBuilder(commandLine)
                    .directory(Path.of(System.getProperty("user.dir")).toFile())
                    .redirectErrorStream(true)
                    .start();

            var output = new String(process.getInputStream().readAllBytes());
            var completed = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (!completed) {
                process.destroyForcibly();
                return output + "\nError: Command timed out after %ds".formatted(timeoutSeconds);
            }

            var exitCode = process.exitValue();
            if (exitCode != 0) {
                return output + "\nError: Command exited with code %d".formatted(exitCode);
            }

            return output.strip();
        } catch (IOException e) {
            return "Error: Command execution failed: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: Command interrupted";
        }
    }

    private static List<String> buildCommandLine(String command, String arguments) {
        var parts = new ArrayList<String>();
        parts.addAll(tokenize(command));
        if (arguments != null && !arguments.isBlank()) {
            parts.addAll(tokenize(arguments));
        }
        return parts;
    }

    private static List<String> tokenize(String input) {
        return Arrays.stream(input.trim().split(" "))
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
