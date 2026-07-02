package airhacks.zsmith.agent.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.agent.Turn")
@Label("Agent Turn")
@Category({"zsmith", "agent"})
@Description("One iteration of the chat loop: Claude invocation plus optional tool execution")
public class AgentTurnEvent extends Event {

    @Label("Agent Name")
    public String agentName;

    @Label("Iteration")
    public int iteration;

    @Label("Stop Reason")
    public String stopReason;

    @Label("Tool Use Count")
    public int toolUseCount;

    @Label("Parallel Tool Count")
    public int parallelToolCount;

    @Label("Sequential Tool Count")
    public int sequentialToolCount;

    @Label("Terminal")
    public boolean terminal;
}
