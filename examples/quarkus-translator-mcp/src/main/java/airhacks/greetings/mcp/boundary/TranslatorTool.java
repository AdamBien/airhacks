package airhacks.greetings.mcp.boundary;

import airhacks.greetings.mcp.control.Translator;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TranslatorTool {
    

    @Inject
    Translator translator;


    @Tool(title = "English to German translator",description = "*MUST BE USED* english to german translator") 
    public ToolResponse translate(@ToolArg(description = "phrase to translate", defaultValue = "test") String name) {
        System.out.println("MCP tool called with: " + name);
        var greetings = this.translator.translate(name);
        return ToolResponse.success(new TextContent(greetings));
    }
}
