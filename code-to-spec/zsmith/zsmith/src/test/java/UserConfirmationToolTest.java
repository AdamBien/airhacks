import org.json.JSONObject;

import airhacks.zsmith.tools.control.UserConfirmationTool;

void main() {
    // tool name is "user_confirmation"
    var tool = UserConfirmationTool.create(prompt -> "yes");
    assert "user_confirmation".equals(tool.toolName()) : "expected 'user_confirmation' but got: " + tool.toolName();

    // description is non-empty
    assert tool.description() != null && !tool.description().isBlank() : "description should be non-empty";

    // input schema contains question and required
    var schema = tool.inputSchema().toString();
    assert schema.contains("\"question\"") : "inputSchema should contain '\"question\"'";
    assert schema.contains("\"required\"") : "inputSchema should contain '\"required\"'";

    // missing question throws IllegalArgumentException
    try {
        tool.execute(new JSONObject());
        throw new AssertionError("expected IllegalArgumentException for missing question");
    } catch (IllegalArgumentException expected) {
    }

    // empty question throws IllegalArgumentException
    try {
        tool.execute(new JSONObject().put("question", ""));
        throw new AssertionError("expected IllegalArgumentException for empty question");
    } catch (IllegalArgumentException expected) {
    }

    // "yes" input returns "yes"
    var yesResult = UserConfirmationTool.create(prompt -> "yes")
            .execute(new JSONObject().put("question", "Proceed?"));
    assert "yes".equals(yesResult) : "expected 'yes' but got: " + yesResult;

    // "y" input returns "yes"
    var yResult = UserConfirmationTool.create(prompt -> "y")
            .execute(new JSONObject().put("question", "Proceed?"));
    assert "yes".equals(yResult) : "expected 'yes' but got: " + yResult;

    // non-affirmative input returns "no"
    var maybeResult = UserConfirmationTool.create(prompt -> "maybe")
            .execute(new JSONObject().put("question", "Proceed?"));
    assert "no".equals(maybeResult) : "expected 'no' but got: " + maybeResult;

    // explicit "no" returns "no"
    var noResult = UserConfirmationTool.create(prompt -> "no")
            .execute(new JSONObject().put("question", "Proceed?"));
    assert "no".equals(noResult) : "expected 'no' but got: " + noResult;
}
