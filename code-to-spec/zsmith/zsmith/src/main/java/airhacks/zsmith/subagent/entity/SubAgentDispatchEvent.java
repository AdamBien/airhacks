package airhacks.zsmith.subagent.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.subagent.Dispatch")
@Label("Sub-Agent Dispatch")
@Category({"zsmith", "subagent"})
@Description("Single delegation of a task to a sub-agent")
public class SubAgentDispatchEvent extends Event {

    @Label("Child Agent")
    public String childAgent;

    @Label("Mode")
    public String mode;

    @Label("Depth")
    public int depth;

    @Label("First Run")
    public boolean firstRun;

    @Label("Outcome")
    public String outcome;

    @Label("Task Size")
    public int taskSize;
}
