package airhacks.zsmith.tools.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.tools.Invocation")
@Label("Tool Invocation")
@Category({"zsmith", "tools"})
@Description("Single tool execution requested by the model")
public class ToolInvocationEvent extends Event {

    @Label("Agent Name")
    public String agentName;

    @Label("Tool Name")
    public String toolName;

    @Label("Outcome")
    public String outcome;

    @Label("Result Size")
    public int resultSize;
}
