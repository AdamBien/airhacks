package airhacks.zsmith.memory.entity;

import org.json.JSONArray;
import org.json.JSONObject;

public record Message(String role, Object content) {

    public JSONObject toJSON() {
        var json = new JSONObject().put("role", this.role);
        if (this.content instanceof String s) {
            json.put("content", s);
        } else if (this.content instanceof JSONArray arr) {
            json.put("content", arr);
        } else {
            json.put("content", this.content);
        }
        return json;
    }

    public static Message fromJSON(JSONObject json) {
        var role = json.getString("role");
        var content = json.get("content");
        return new Message(role, content);
    }

    public static Message user(String content) {
        return new Message("user", content);
    }

    public static Message assistant(String content) {
        return new Message("assistant", content);
    }

    public static Message withContentBlocks(String role, JSONArray contentBlocks) {
        return new Message(role, contentBlocks);
    }
}
