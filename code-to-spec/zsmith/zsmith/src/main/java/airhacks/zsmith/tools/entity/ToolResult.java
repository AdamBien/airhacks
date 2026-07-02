package airhacks.zsmith.tools.entity;

import org.json.JSONObject;

public record ToolResult(String toolUseId, String content, boolean isError) {

    public static ToolResult success(String toolUseId, String content) {
        return new ToolResult(toolUseId, content, false);
    }

    public static ToolResult error(String toolUseId, String errorMessage) {
        return new ToolResult(toolUseId, errorMessage, true);
    }

    public JSONObject toContentBlock() {
        var block = new JSONObject()
                .put("type", "tool_result")
                .put("tool_use_id", this.toolUseId)
                .put("content", this.content);
        if (this.isError) {
            block.put("is_error", true);
        }
        return block;
    }
}
