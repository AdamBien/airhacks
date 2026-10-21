package airhacks.qmp.yieldplanning.entity;

import static java.util.Map.entry;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public record YieldPlan(LocalDate day, long tonnes, int stoneSize) {

    public static final String DAY = "day";
    public static final String TONNES = "tonnes";
    public static final String STONE_SIZE = "stoneSize";

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add(DAY, this.day.toString())
                .add(TONNES, this.tonnes)
                .add(STONE_SIZE, this.stoneSize)
                .build();
    }

    public static YieldPlan fromJSON(JsonObject json) {
        var day = day(json.get(DAY));
        var tonnes = tonnes(json.get(TONNES));
        var stoneSize = stoneSize(json.get(STONE_SIZE));
        if (day.isEmpty() || tonnes.isEmpty() || stoneSize.isEmpty())
            throw invalidInput(entry(DAY, day), entry(TONNES, tonnes), entry(STONE_SIZE, stoneSize));
        return new YieldPlan(day.get(), tonnes.get(), stoneSize.get());
    }

    /// Validated before the plan is looked up, so an invalid revision is rejected regardless of the day's state.
    /// Fields absent from the revision keep the plan's current values.
    public static UnaryOperator<YieldPlan> revision(JsonObject json) {
        var submitted = Stream.of(TONNES, STONE_SIZE)
                .filter(json::containsKey)
                .toList();
        if (submitted.isEmpty())
            throw invalidInput(List.of(TONNES, STONE_SIZE));
        var invalid = submitted.stream()
                .filter(field -> !isValid(field, json.get(field)))
                .toList();
        if (!invalid.isEmpty())
            throw invalidInput(invalid);
        return plan -> plan.revisedBy(json);
    }

    YieldPlan revisedBy(JsonObject revision) {
        return new YieldPlan(this.day,
                tonnes(revision.get(TONNES)).orElse(this.tonnes),
                stoneSize(revision.get(STONE_SIZE)).orElse(this.stoneSize));
    }

    static boolean isValid(String field, JsonValue value) {
        return switch (field) {
            case TONNES -> tonnes(value).isPresent();
            case STONE_SIZE -> stoneSize(value).isPresent();
            default -> false;
        };
    }

    /// Names every field whose parsed value is empty, so a client can fix the whole request in one round trip.
    @SafeVarargs
    public static BadRequestException invalidInput(Map.Entry<String, ? extends Optional<?>>... fields) {
        return invalidInput(Stream.of(fields)
                .filter(field -> field.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList());
    }

    public static BadRequestException invalidInput(List<String> fields) {
        var body = Json.createObjectBuilder()
                .add("invalid", Json.createArrayBuilder(fields))
                .build();
        return new BadRequestException("invalid: " + fields, Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(body.toString())
                .build());
    }

    public static Optional<LocalDate> day(JsonValue value) {
        return value instanceof JsonString string ? day(string.getString()) : Optional.empty();
    }

    public static Optional<LocalDate> day(String value) {
        if (value == null)
            return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(value));
        } catch (DateTimeParseException _) {
            return Optional.empty();
        }
    }

    /// Whole millimetres above zero only.
    static Optional<Integer> stoneSize(JsonValue value) {
        if (!(value instanceof JsonNumber number))
            return Optional.empty();
        try {
            return Optional.of(number.intValueExact()).filter(millimetres -> millimetres > 0);
        } catch (ArithmeticException _) {
            return Optional.empty();
        }
    }

    /// Whole, non-negative tonnes only; `12500.0` counts as whole, `12500.5` does not.
    static Optional<Long> tonnes(JsonValue value) {
        if (!(value instanceof JsonNumber number))
            return Optional.empty();
        try {
            return Optional.of(number.longValueExact()).filter(tonnes -> tonnes >= 0);
        } catch (ArithmeticException _) {
            return Optional.empty();
        }
    }
}
