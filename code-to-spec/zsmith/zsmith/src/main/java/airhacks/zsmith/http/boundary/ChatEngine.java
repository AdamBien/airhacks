package airhacks.zsmith.http.boundary;

@FunctionalInterface
public interface ChatEngine {

    String chat(String sessionId, String message);
}
