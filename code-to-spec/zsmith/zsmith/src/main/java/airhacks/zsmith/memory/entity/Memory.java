package airhacks.zsmith.memory.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public record Memory(List<Message> messages) {

    public Memory {
        messages = new ArrayList<>(messages);
    }

    public Memory() {
        this(new ArrayList<>());
    }

    public void addUserMessage(String content) {
        this.messages.add(Message.user(content));
    }

    public void addAssistantMessage(String content) {
        this.messages.add(Message.assistant(content));
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void clear() {
        this.messages.clear();
    }

    public int size() {
        return this.messages.size();
    }

    public JSONArray toJSON() {
        var array = new JSONArray();
        this.messages.stream()
                .map(Message::toJSON)
                .forEach(array::put);
        return array;
    }

    public static Memory fromJSON(JSONArray json) {
        var messages = new ArrayList<Message>();
        for (int i = 0; i < json.length(); i++) {
            var obj = json.getJSONObject(i);
            messages.add(Message.fromJSON(obj));
        }
        return new Memory(messages);
    }
}
