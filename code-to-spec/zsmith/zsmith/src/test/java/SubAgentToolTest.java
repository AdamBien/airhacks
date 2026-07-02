import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.subagent.control.SubAgentTool;

void main() throws Exception {
    var child = new Agent("researcher", "You are a research assistant.");
    var tool = new SubAgentTool(child);

    // tool name derived from agent name
    assert "delegate_to_researcher".equals(tool.toolName())
            : "expected 'delegate_to_researcher' but got: " + tool.toolName();

    // description contains agent name
    assert tool.description().contains("researcher")
            : "description should mention agent name: " + tool.description();

    // input schema is valid
    assert tool.inputSchema().has("properties") : "inputSchema should have properties";
    assert tool.inputSchema().toString().contains("task") : "inputSchema should have 'task' field";

    // tool definition shape
    var definition = tool.toToolDefinition();
    assert "delegate_to_researcher".equals(definition.getString("name"));
    assert definition.has("input_schema");
    assert definition.has("description");

    // custom name and description
    var customTool = new SubAgentTool(child, "ask_expert", "Asks the domain expert");
    assert "ask_expert".equals(customTool.toolName())
            : "expected 'ask_expert' but got: " + customTool.toolName();
    assert "Asks the domain expert".equals(customTool.description())
            : "unexpected description: " + customTool.description();

    // depth guard
    var shallowTool = new SubAgentTool(child, "shallow", "test", 0);
    var result = shallowTool.execute(new org.json.JSONObject().put("task", "test"));
    assert result.contains("Error") && result.contains("depth")
            : "expected depth error but got: " + result;

    // adaptive parallel: first run sequential, subsequent runs parallel
    var sandboxHome = Files.createTempDirectory("zsmith-subagent-test");
    var originalHome = System.getProperty("user.home");
    System.setProperty("user.home", sandboxHome.toString());
    try {
        var firstRunChild = new Agent("first-run-probe", "test");
        var firstRunTool = new SubAgentTool(firstRunChild);

        var markerPath = Path.of(sandboxHome.toString(), "." + ZCfg.APP_NAME,
                "first-run-probe", ".first_run_completed");
        Files.deleteIfExists(markerPath);

        // runParallel=true but no marker yet → sequential
        assert !firstRunTool.parallel()
                : "expected parallel()=false on first run (no marker)";

        // simulate completion of a first successful run
        Files.createDirectories(markerPath.getParent());
        Files.writeString(markerPath, "");
        assert firstRunTool.parallel()
                : "expected parallel()=true once marker exists";

        // sequential override always wins, even with marker
        var sequentialTool = new SubAgentTool(firstRunChild, false);
        assert !sequentialTool.parallel()
                : "expected parallel()=false when runParallel=false regardless of marker";
    } finally {
        System.setProperty("user.home", originalHome);
    }
}
