package airhacks.zsmith.tools.entity;

import org.json.JSONObject;

public record ToolUse(String id, String name, JSONObject input) {

    public static ToolUse fromJSON(JSONObject json) {
        var id = json.getString("id");
        var name = json.getString("name");
        var input = json.optJSONObject("input");
        return new ToolUse(id, name, input != null ? input : new JSONObject());
    }

    public static boolean isToolUse(JSONObject contentBlock) {
        return "tool_use".equals(contentBlock.optString("type"));
    }
}
