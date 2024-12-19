package airhacks.sentimental.entity;

public class HallucinationException extends IllegalStateException{
    
    public HallucinationException(String response) {
        super("unexpected: " + response);
    }
    
}
