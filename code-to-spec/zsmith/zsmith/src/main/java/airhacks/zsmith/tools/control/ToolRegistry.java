package airhacks.zsmith.tools.control;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import airhacks.zsmith.tools.entity.Tool;

public class ToolRegistry {

    Map<String, Entry> byName = new LinkedHashMap<>();

    public ToolRegistry register(Class<? extends RecordTool> toolClass) {
        if (!toolClass.isRecord())
            throw new IllegalArgumentException(toolClass + " must be a record");
        var def = toolClass.getAnnotation(Tool.class);
        if (def == null)
            throw new IllegalArgumentException(toolClass + " missing @Tool");
        var components = toolClass.getRecordComponents();
        var name = def.name().isBlank() ? deriveName(toolClass) : def.name();
        byName.put(name, new Entry(
                name, def.description(),
                Schemas.fromComponents(components),
                canonicalConstructor(toolClass, components),
                components));
        return this;
    }

    static String deriveName(Class<?> toolClass) {
        var simpleName = toolClass.getSimpleName();
        var base = simpleName.endsWith("Tool")
                ? simpleName.substring(0, simpleName.length() - "Tool".length())
                : simpleName;
        return toSnakeCase(base);
    }

    static String toSnakeCase(String camelCase) {
        return camelCase.codePoints()
                .collect(StringBuilder::new,
                        ToolRegistry::appendSnake,
                        StringBuilder::append)
                .toString();
    }

    static void appendSnake(StringBuilder out, int codePoint) {
        if (Character.isUpperCase(codePoint) && !out.isEmpty()) out.append('_');
        out.appendCodePoint(Character.toLowerCase(codePoint));
    }

    public String invoke(String name, JSONObject input) {
        var entry = byName.get(name);
        if (entry == null) throw new IllegalArgumentException("unknown tool: " + name);
        var args = Stream.of(entry.components)
                .map(c -> Values.coerce(input.opt(c.getName()), c.getType()))
                .toArray();
        try {
            return ((RecordTool) entry.ctor.newInstance(args)).execute();
        } catch (ReflectiveOperationException x) {
            throw new IllegalStateException("tool %s failed".formatted(name), x);
        }
    }

    public JSONArray toolDefinitions() {
        return new JSONArray(byName.values().stream().map(Entry::toJson).toList());
    }

    static Constructor<?> canonicalConstructor(Class<?> recordClass, RecordComponent[] components) {
        var types = Stream.of(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
        try {
            var ctor = recordClass.getDeclaredConstructor(types);
            ctor.setAccessible(true);
            return ctor;
        } catch (NoSuchMethodException x) {
            throw new IllegalStateException("no canonical constructor on " + recordClass, x);
        }
    }

    record Entry(String name, String description, JSONObject schema,
                 Constructor<?> ctor, RecordComponent[] components) {

        JSONObject toJson() {
            return new JSONObject()
                    .put("name", name)
                    .put("description", description)
                    .put("input_schema", schema);
        }
    }
}
