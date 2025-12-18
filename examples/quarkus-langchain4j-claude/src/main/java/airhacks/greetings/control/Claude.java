package airhacks.greetings.control;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.mcp.runtime.McpToolBox;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface Claude {
    @McpToolBox("greeter")
    String chat(String userMessage);
}
