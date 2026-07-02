package airhacks.zsmith.systemprompt.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads a system prompt from system.prompt files in order:
 * 1. ~/.{appName}/{agentName}/system.prompt (global agent-specific)
 * 2. ./{agentName}/system.prompt (local agent-specific)
 * 3. ./system.prompt (highest priority)
 *
 * Each layer overwrites the previous.
 */
public class SystemPromptLoader {

    static final String SYSTEM_PROMPT_FILE = "system.prompt";

    public static String load(String appName, String agentName) {
        var userHome = System.getProperty("user.home");
        String prompt = null;
        var globalPrompt = Path.of(userHome, "." + appName, agentName, SYSTEM_PROMPT_FILE);
        if (Files.exists(globalPrompt)) {
            prompt = readTextFile(globalPrompt);
        }
        var localPrompt = Path.of(agentName, SYSTEM_PROMPT_FILE);
        if (Files.exists(localPrompt)) {
            prompt = readTextFile(localPrompt);
        }
        var basePrompt = Path.of(SYSTEM_PROMPT_FILE);
        if (Files.exists(basePrompt)) {
            prompt = readTextFile(basePrompt);
        }
        return prompt;
    }

    static String readTextFile(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file: " + file, e);
        }
    }
}
