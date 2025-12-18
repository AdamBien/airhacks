package airhacks.greetings.mcp.boundary;

import airhacks.greetings.mcp.control.Greeter;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GreetingTool {
    

    @Inject
    Greeter greeter;


    @Tool(title = "Developer Greeter",description = "greets developers") 
    public ToolResponse hello(@ToolArg(description = "Developer's name", defaultValue = "duke") String name) {
        System.out.println("MCP tool called with: " + name);
        var greetings = this.greeter.hello(name);
        return ToolResponse.success(new TextContent(greetings));
    }
}
