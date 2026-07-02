import java.util.Objects;

import org.json.JSONObject;

import airhacks.zsmith.tools.control.LinkCheckerTool;

void main() {
    var tool = LinkCheckerTool.create();

    // tool name is "check_link"
    assert "check_link".equals(tool.toolName()) : "expected 'check_link' but got: " + tool.toolName();

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

    // malformed url returns error
    var malformedResult = tool.execute(new JSONObject().put("url", "not a valid url"));
    assert malformedResult.startsWith("Error:") : "expected error for malformed url but got: " + malformedResult;

    // url without scheme returns "Error: Invalid URL"
    var noScheme = tool.execute(new JSONObject().put("url", "missing-scheme"));
    assert "Error: Invalid URL".equals(noScheme) : "expected 'Error: Invalid URL' but got: " + noScheme;
}
