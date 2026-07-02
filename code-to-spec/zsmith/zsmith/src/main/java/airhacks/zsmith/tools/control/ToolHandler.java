package airhacks.zsmith.tools.control;

import java.util.List;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ToolHandler {

    String toolName();

    String description();

    JSONObject inputSchema();

    String execute(JSONObject input);

    default boolean parallel() { return false; }

    default JSONObject toToolDefinition() {
        return new JSONObject()
                .put("name", toolName())
                .put("description", description())
                .put("input_schema", inputSchema());
    }

    record Prop<E extends Enum<E>>(E name, String type, String description, List<String> enumValues, boolean required) {

        public static <E extends Enum<E>> Prop<E> string(E name, String description) {
            return new Prop<>(name, "string", description, List.of(), true);
        }

        public static <E extends Enum<E>> Prop<E> stringEnum(E name, String description, String... values) {
            return new Prop<>(name, "string", description, List.of(values), true);
        }

        public static <E extends Enum<E>> Prop<E> number(E name, String description) {
            return new Prop<>(name, "number", description, List.of(), true);
        }

        public static <E extends Enum<E>> Prop<E> integer(E name, String description) {
            return new Prop<>(name, "integer", description, List.of(), true);
        }

        public Prop<E> optional() {
            return new Prop<>(name, type, description, enumValues, false);
        }
    }

    static ToolHandler of(String name, String description, JSONObject inputSchema,
                   Function<JSONObject, String> execute) {
        return of(name, description, inputSchema, execute, false);
    }

    static ToolHandler of(String name, String description,
                   Function<JSONObject, String> execute) {
        return of(name, description, emptySchema(), execute, false);
    }

    static ToolHandler of(String name, String description, JSONObject inputSchema,
                   Function<JSONObject, String> execute, boolean parallel) {
        record SimpleTool(String toolName, String description, JSONObject inputSchema,
                          Function<JSONObject, String> fn, boolean parallel) implements ToolHandler {
            @Override
            public String execute(JSONObject input) { return fn.apply(input); }
        }
        return new SimpleTool(name, description, inputSchema, execute, parallel);
    }

    static JSONObject emptySchema() {
        return new JSONObject()
                .put("type", "object")
                .put("properties", new JSONObject());
    }

    static JSONObject schema(Prop<?>... props) {
        var properties = new JSONObject();
        var required = new JSONArray();
        for (var prop : props) {
            var p = new JSONObject()
                    .put("type", prop.type())
                    .put("description", prop.description());
            if (!prop.enumValues().isEmpty()) {
                p.put("enum", new JSONArray(prop.enumValues()));
            }
            properties.put(prop.name().name(), p);
            if (prop.required()) {
                required.put(prop.name().name());
            }
        }
        var schema = new JSONObject()
                .put("type", "object")
                .put("properties", properties);
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        return schema;
    }
}
