package airhacks.sentimental.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import airhacks.logging.control.Log;

public class SentimentAnalysisTest {

    @Test
    void analyze() throws IOException {
        var result = SentimentAnalysis.analyze("java is hot and tastes really good");
        Log.info(result);
        assertThat(result.isPositive()).isTrue();
        
        result = SentimentAnalysis.analyze("python is dangerous, unpredictable and slow");
        Log.info(result);
        assertThat(result.isNegative()).isTrue();
        
        result = SentimentAnalysis.analyze("old rusty engine");
        Log.info(result);
        assertThat(result.isNegative()).isTrue();

        result = SentimentAnalysis.analyze("where should I GO?");
        Log.info(result);
        assertThat(result.isNeutral()).isTrue();
    }
}
