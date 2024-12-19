package airhacks.sentimental.boundary;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import airhacks.logging.control.Log;
import airhacks.sentimental.control.ModelLoader;
import airhacks.sentimental.entity.Result;

public interface SentimentAnalysis {

    String systemPrompt = """
                    Your task is to analyze whether a given statement is positive or negative. 
                    Only respond with either: "positive", "negative" or "neutral".                        

                    Analyze the following statement:

            """;

    static String invoke(String message) throws IOException {
        var model = ModelLoader.load();
        var promptContext = model.promptSupport()
                .get()
                .builder()
                .addSystemMessage(systemPrompt)
                .addUserMessage(message)
                .build();

        var response = model.generate(UUID.randomUUID(), promptContext, 0.0f, 256, (s, f) -> {});
        var duration = response.promptTimeMs;
        Log.info("responded in: %d ms".formatted(duration));
        return response.responseText;
    }

    public static Result analyze(String message){
        var start = Instant.now();
        String response;
        try {
            response = invoke(message);

        } catch (IOException e) {
            throw new RuntimeException("cannot invoke model. Reason: " + e);
        }
        var duration = Duration.between(start, Instant.now());
        return Result.fromLLMResponse(duration,response);
    }


}
