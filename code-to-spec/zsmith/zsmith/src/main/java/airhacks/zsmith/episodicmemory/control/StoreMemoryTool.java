package airhacks.zsmith.episodicmemory.control;

import org.json.JSONObject;

import airhacks.zsmith.episodicmemory.boundary.EpisodicMemoryStore;
import airhacks.zsmith.episodicmemory.entity.Episode;
import airhacks.zsmith.episodicmemory.entity.MemoryType;
import airhacks.zsmith.tools.control.ToolHandler;

public class StoreMemoryTool implements ToolHandler {

    private final EpisodicMemoryStore store;

    public StoreMemoryTool(EpisodicMemoryStore store) {
        this.store = store;
    }

    @Override
    public String toolName() {
        return "store_memory";
    }

    @Override
    public String description() {
        return """
                Stores an episode in long-term memory for future recall. \
                Each memory must be classified with a type: \
                'user' for information about the user's role, preferences, and knowledge; \
                'feedback' for guidance or corrections the user has given; \
                'project' for ongoing work, goals, decisions, or incidents; \
                'reference' for pointers to external resources and systems.""";
    }

    enum Field { content, type }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(
                Prop.string(Field.content, "The information to remember"),
                Prop.stringEnum(Field.type, "The memory type: user, feedback, project, or reference",
                        "user", "feedback", "project", "reference")
        );
    }

    @Override
    public String execute(JSONObject input) {
        var content = input.getString(Field.content.name());
        var type = MemoryType.fromString(input.getString(Field.type.name()));
        var episode = new Episode(content, null, type);
        store.store(episode);
        return "Memory stored.";
    }
}
