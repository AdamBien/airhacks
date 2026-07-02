import java.util.Objects;

import org.json.JSONObject;

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.boundary.Tools;
import airhacks.zsmith.tools.control.FetchUrlTool;

void main() {
    var tool = FetchUrlTool.create();

    // tool name is "fetch_url"
    assert "fetch_url".equals(tool.toolName()) : "expected 'fetch_url' but got: " + tool.toolName();

    // description is non-empty
    Objects.requireNonNull(tool.description(), "description should not be null");
    assert !tool.description().isBlank() : "description should be non-empty";

    // input schema contains url and required
    var schema = tool.inputSchema().toString();
    assert schema.contains("\"url\"") : "inputSchema should contain '\"url\"'";
    assert schema.contains("\"required\"") : "inputSchema should contain '\"required\"'";

    // missing url returns error
    var missingResult = tool.execute(new JSONObject());
    assert "Error: Missing required parameter: url".equals(missingResult) : "expected error for missing url but got: " + missingResult;

    // url without scheme returns "Error: Invalid URL"
    var noScheme = tool.execute(new JSONObject().put("url", "missing-scheme"));
    assert "Error: Invalid URL".equals(noScheme) : "expected 'Error: Invalid URL' but got: " + noScheme;

    // parallel execution is enabled
    assert tool.parallel() : "fetch_url should be parallel-safe";

    // can be registered via withTool() and via Tools enum
    var direct = new Agent().withSystemPrompt("hi").withTool(FetchUrlTool.create());
    assert direct.tools().containsKey("fetch_url") : "agent should contain 'fetch_url' tool";

    var enumAgent = new Agent().withSystemPrompt("hi").withTools(Tools.FETCH_URL);
    assert enumAgent.tools().containsKey("fetch_url") : "agent should contain 'fetch_url' via enum";
}
