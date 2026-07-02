package airhacks.zsmith.tools.control;

import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import airhacks.zsmith.tools.entity.Describe;

interface Schemas {

    static JSONObject fromComponents(RecordComponent[] components) {
        var properties = Stream.of(components).collect(Collectors.toMap(
                RecordComponent::getName, Schemas::propertyJson,
                (a, b) -> a, LinkedHashMap::new));
        var required = Stream.of(components)
                .filter(Schemas::isRequired)
                .map(RecordComponent::getName)
                .toList();
        var schema = new JSONObject()
                .put("type", "object")
                .put("properties", new JSONObject(properties));
        if (!required.isEmpty()) schema.put("required", new JSONArray(required));
        return schema;
    }

    static JSONObject propertyJson(RecordComponent component) {
        var property = new JSONObject().put("type", jsonType(component.getType()));
        var describe = component.getAnnotation(Describe.class);
        if (describe != null) property.put("description", describe.value());
        return property;
    }

    static boolean isRequired(RecordComponent component) {
        return component.getType().isPrimitive();
    }

    static String jsonType(Class<?> type) {
        return switch (type.getName()) {
            case "java.lang.String" -> "string";
            case "boolean", "java.lang.Boolean" -> "boolean";
            case "int", "long", "java.lang.Integer", "java.lang.Long" -> "integer";
            case "double", "float", "java.lang.Double", "java.lang.Float" -> "number";
            default -> "string";
        };
    }
}
