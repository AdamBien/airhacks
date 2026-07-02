import org.json.JSONObject;

import airhacks.zsmith.tools.control.LaunchAppTool;

void main() {
    var tool = LaunchAppTool.create("open_in_editor", "Opens a file in VS Code", "echo");

    // tool definition
    assert "open_in_editor".equals(tool.toolName()) : "expected 'open_in_editor' but got: " + tool.toolName();
    assert "Opens a file in VS Code".equals(tool.description()) : "unexpected description: " + tool.description();
    assert tool.inputSchema().has("properties") : "inputSchema should have properties";

    // execute with arguments
    var result = tool.execute(new JSONObject().put("arguments", "hello world"));
    assert "hello world".equals(result) : "expected 'hello world' but got: " + result;

    // execute with empty arguments
    var emptyResult = tool.execute(new JSONObject().put("arguments", ""));
    assert emptyResult.isEmpty() : "expected empty output but got: " + emptyResult;

    // execute without arguments key
    var noArgResult = tool.execute(new JSONObject());
    assert noArgResult.isEmpty() : "expected empty output but got: " + noArgResult;

    // non-existent command
    var badTool = LaunchAppTool.create("bad", "bad", "/nonexistent/command");
    var badResult = badTool.execute(new JSONObject().put("arguments", "test"));
    assert badResult.contains("Error") : "expected error but got: " + badResult;

    // command string may carry inline flags — they must be tokenised and executed
    var inlineFlagsTool = LaunchAppTool.create("echo_with_flag", "echo with -n", "echo -n");
    var inlineResult = inlineFlagsTool.execute(new JSONObject().put("arguments", "hello"));
    assert "hello".equals(inlineResult) : "expected 'hello' (echo -n suppresses newline) but got: " + inlineResult;

}
