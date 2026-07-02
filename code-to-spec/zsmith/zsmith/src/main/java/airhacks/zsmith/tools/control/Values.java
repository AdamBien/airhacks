package airhacks.zsmith.tools.control;

import org.json.JSONObject;

interface Values {

    static Object coerce(Object value, Class<?> targetType) {
        if (value == null || value == JSONObject.NULL) return defaultFor(targetType);
        return switch (targetType.getName()) {
            case "java.lang.String" -> value.toString();
            case "int", "java.lang.Integer" -> ((Number) value).intValue();
            case "long", "java.lang.Long" -> ((Number) value).longValue();
            case "double", "java.lang.Double",
                 "float", "java.lang.Float" -> ((Number) value).doubleValue();
            case "boolean", "java.lang.Boolean" -> (Boolean) value;
            default -> value;
        };
    }

    static Object defaultFor(Class<?> targetType) {
        if (!targetType.isPrimitive()) return null;
        return switch (targetType.getName()) {
            case "boolean" -> false;
            case "int" -> 0;
            case "long" -> 0L;
            default -> 0.0;
        };
    }
}
