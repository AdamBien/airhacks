package airhacks.zsmith.subagent.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.subagent.entity.SubAgentDispatchEvent;
import airhacks.zsmith.tools.control.ToolHandler;

public class SubAgentTool implements ToolHandler {

    static final int DEFAULT_MAX_DEPTH = 3;
    static final String FIRST_RUN_MARKER = ".first_run_completed";
    static final ScopedValue<Integer> DEPTH = ScopedValue.newInstance();

    private final Agent subAgent;
    private final String name;
    private final String toolDescription;
    private final int maxDepth;
    private final boolean runParallel;

    public SubAgentTool(Agent subAgent, String name, String description, int maxDepth, boolean parallel) {
        this.subAgent = subAgent;
        this.name = name;
        this.toolDescription = description;
        this.maxDepth = maxDepth;
        this.runParallel = parallel;
    }

    public SubAgentTool(Agent subAgent, String name, String description, int maxDepth) {
        this(subAgent, name, description, maxDepth, true);
    }

    public SubAgentTool(Agent subAgent, String name, String description) {
        this(subAgent, name, description, DEFAULT_MAX_DEPTH);
    }

    public SubAgentTool(Agent subAgent, boolean parallel) {
        this(subAgent,
                "delegate_to_" + subAgent.name(),
                "Delegates a task to the '%s' sub-agent. Send a clear, complete task description and the sub-agent will work on it independently and return the result."
                        .formatted(subAgent.name()),
                DEFAULT_MAX_DEPTH,
                parallel);
    }

    public SubAgentTool(Agent subAgent) {
        this(subAgent, true);
    }

    @Override
    public String toolName() {
        return this.name;
    }

    @Override
    public String description() {
        return this.toolDescription;
    }

    enum Field {
        task
    }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(
                Prop.string(Field.task, "The task to delegate to the sub-agent. Be specific and complete."));
    }

    @Override
    public boolean parallel() {
        return this.runParallel && firstRunCompleted();
    }

    @Override
    public String execute(JSONObject input) {
        var event = new SubAgentDispatchEvent();
        event.childAgent = this.subAgent.name();
        event.depth = DEPTH.orElse(0);
        var firstRunDone = firstRunCompleted();
        event.firstRun = !firstRunDone;
        event.mode = (this.runParallel && firstRunDone) ? "parallel" : "sequential";
        event.begin();
        try {
            if (event.depth >= this.maxDepth) {
                event.outcome = "depth_exceeded";
                return "Error: Maximum sub-agent depth (%d) exceeded".formatted(this.maxDepth);
            }
            var taskKey = Field.task.name();
            if (!input.has(taskKey) || input.optString(taskKey, "").isBlank()) {
                Log.subagent("sub-agent '%s' invoked without '%s' — received keys: %s"
                        .formatted(this.subAgent.name(), taskKey, input.keySet()));
                event.outcome = "missing_task";
                return "Error: missing required '%s' field. Provide the full task description as a string under '%s'."
                        .formatted(taskKey, taskKey);
            }
            var task = input.getString(taskKey);
            event.taskSize = task.length();
            Log.subagent("delegating to sub-agent '%s': %s".formatted(this.subAgent.name(), task));
            try {
                var result = ScopedValue.where(DEPTH, event.depth + 1)
                        .call(() -> this.subAgent.chat(task));
                Log.subagent("sub-agent '%s' completed".formatted(this.subAgent.name()));
                markFirstRunCompleted();
                event.outcome = "success";
                return result;
            } catch (Exception e) {
                event.outcome = "error";
                return "Error: Sub-agent '%s' failed: %s".formatted(this.subAgent.name(), e.getMessage());
            }
        } finally {
            if (event.shouldCommit()) {
                event.commit();
            }
        }
    }

    boolean firstRunCompleted() {
        return Files.exists(markerPath());
    }

    void markFirstRunCompleted() {
        var path = markerPath();
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.writeString(path, "");
            }
        } catch (IOException e) {
            Log.warning("could not write first-run marker for "
                    + this.subAgent.name() + ": " + e.getMessage());
        }
    }

    Path markerPath() {
        var userHome = System.getProperty("user.home");
        return Path.of(userHome, "." + ZCfg.APP_NAME, this.subAgent.name(), FIRST_RUN_MARKER);
    }
}
