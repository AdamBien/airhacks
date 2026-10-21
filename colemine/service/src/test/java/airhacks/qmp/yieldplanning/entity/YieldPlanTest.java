package airhacks.qmp.yieldplanning.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.json.Json;
import jakarta.json.JsonValue;

class YieldPlanTest {

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(nullValues = "absent", value = {
            "12500, 12500",
            "0, 0",
            "12500.0, 12500",
            "-1, absent",
            "12500.5, absent",
            "'\"12500\"', absent",
            "null, absent",
            "99999999999999999999, absent"
    })
    void tonnes(String json, Long expected) {
        assertThat(YieldPlan.tonnes(parse(json))).isEqualTo(Optional.ofNullable(expected));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(nullValues = "absent", value = {
            "2026-10-22, 2026-10-22",
            "2026-02-30, absent",
            "22.10.2026, absent",
            "absent, absent"
    })
    void day(String text, String expected) {
        assertThat(YieldPlan.day(text).map(Object::toString)).isEqualTo(Optional.ofNullable(expected));
    }

    static JsonValue parse(String json) {
        return Json.createReader(new java.io.StringReader("[" + json + "]")).readArray().get(0);
    }
}
