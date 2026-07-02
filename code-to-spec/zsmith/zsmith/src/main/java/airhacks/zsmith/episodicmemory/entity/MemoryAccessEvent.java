package airhacks.zsmith.episodicmemory.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.memory.Access")
@Label("Memory Access")
@Category({"zsmith", "memory"})
@Description("Read or write of a persistent memory store")
public class MemoryAccessEvent extends Event {

    @Label("Store")
    public String store;

    @Label("Operation")
    public String operation;

    @Label("Episode Count")
    public int episodeCount;

    @Label("Payload Size")
    public int payloadSize;

    @Label("Outcome")
    public String outcome;
}
