package airhacks.petstore.catalog.entity;

import java.util.List;
import java.util.function.Function;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Page<T>(List<T> content, long total) {

    public JsonObject toJSON(Function<T, JsonObject> mapper) {
        var elements = Json.createArrayBuilder();
        this.content.stream().map(mapper).forEach(elements::add);
        return Json.createObjectBuilder()
                .add("content", elements)
                .add("total", this.total)
                .build();
    }
}
