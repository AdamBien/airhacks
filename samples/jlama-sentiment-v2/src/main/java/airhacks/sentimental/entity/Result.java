package airhacks.sentimental.entity;

import java.time.Duration;

public record Result(Duration duration,Sentiment sentiment) {
    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL;
    }

    public static Result fromLLMResponse(Duration duration,String answer) {
        var normalized = answer
                .trim()
                .toUpperCase();
        try{
            var sentiment = Sentiment.valueOf(normalized);
            return new Result(duration,sentiment);
        }catch(IllegalArgumentException ex){
            throw new HallucinationException(answer);
        }
    }

    public boolean isPositive(){
        return Sentiment.POSITIVE.equals(this.sentiment);
    }

    public boolean isNegative(){
        return Sentiment.NEGATIVE.equals(this.sentiment);
    }

    public boolean isNeutral(){
        return Sentiment.NEUTRAL.equals(this.sentiment);
    }
}
