package airhacks.zsmith.lightmetal.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.lightmetal.APICall")
@Label("LightMetal API Call")
@Category({"zsmith", "lightmetal"})
@Description("Single in-process call to the LightMetal Anthropic-shaped API")
public class LightMetalAPICallEvent extends Event {

    @Label("Model")
    public String model;

    @Label("Stop Reason")
    public String stopReason;

    @Label("Input Tokens")
    public int inputTokens;

    @Label("Output Tokens")
    public int outputTokens;
}
