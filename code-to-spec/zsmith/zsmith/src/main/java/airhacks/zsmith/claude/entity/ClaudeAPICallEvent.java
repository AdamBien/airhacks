package airhacks.zsmith.claude.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.claude.APICall")
@Label("Claude API Call")
@Category({"zsmith", "claude"})
@Description("Single HTTP call to the Anthropic Messages API")
public class ClaudeAPICallEvent extends Event {

    @Label("Model")
    public String model;

    @Label("Fallback Attempt")
    public boolean fallback;

    @Label("HTTP Status")
    public int statusCode;

    @Label("Stop Reason")
    public String stopReason;

    @Label("Input Tokens")
    public int inputTokens;

    @Label("Output Tokens")
    public int outputTokens;

    @Label("Cache Read Tokens")
    public int cacheReadTokens;

    @Label("Cache Creation Tokens")
    public int cacheCreationTokens;
}
