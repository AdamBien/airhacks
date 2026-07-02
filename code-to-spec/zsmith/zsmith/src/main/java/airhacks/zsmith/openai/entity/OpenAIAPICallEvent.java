package airhacks.zsmith.openai.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.openai.APICall")
@Label("OpenAI API Call")
@Category({"zsmith", "openai"})
@Description("Single HTTP call to an OpenAI-compatible Chat Completions endpoint")
public class OpenAIAPICallEvent extends Event {

    @Label("Model")
    public String model;

    @Label("HTTP Status")
    public int statusCode;

    @Label("Stop Reason")
    public String stopReason;

    @Label("Input Tokens")
    public int inputTokens;

    @Label("Output Tokens")
    public int outputTokens;
}
