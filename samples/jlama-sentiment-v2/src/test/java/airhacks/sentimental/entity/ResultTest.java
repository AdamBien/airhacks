package airhacks.sentimental.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ResultTest {
    @Test
    void hallucination() {
        var exception = assertThrows(HallucinationException.class, ()-> Result.fromLLMResponse(null, "incredible"));
        assertThat(exception).hasMessageContaining("incredible");

    }

    @Test
    void positive() {
        var result = Result.fromLLMResponse(null, "positive");
        assertThat(result.isPositive()).isTrue();

    }

    @Test
    void negative() {
        var result = Result.fromLLMResponse(null, "negative");
        assertThat(result.isNegative()).isTrue();
    }
}
