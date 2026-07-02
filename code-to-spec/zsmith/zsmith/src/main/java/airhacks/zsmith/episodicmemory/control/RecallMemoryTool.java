package airhacks.zsmith.episodicmemory.control;

import java.util.List;

import org.json.JSONObject;

import airhacks.zsmith.episodicmemory.boundary.EpisodicMemoryStore;
import airhacks.zsmith.episodicmemory.entity.Episode;
import airhacks.zsmith.episodicmemory.entity.MemoryType;
import airhacks.zsmith.tools.control.ToolHandler;

public class RecallMemoryTool implements ToolHandler {

    private final EpisodicMemoryStore store;

    public RecallMemoryTool(EpisodicMemoryStore store) {
        this.store = store;
    }

    @Override
    public String toolName() {
        return "recall_memory";
    }

    @Override
    public String description() {
        return "Recalls past memories. Optionally filter by type (user, feedback, project, reference) or limit to the most recent entries.";
    }

    enum Field { type, limit }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(
                Prop.stringEnum(Field.type, "Optional type to filter memories",
                        "user", "feedback", "project", "reference").optional(),
                Prop.integer(Field.limit, "Maximum number of recent memories to return. Defaults to 10.").optional()
        );
    }

    @Override
    public String execute(JSONObject input) {
        var typeString = input.optString(Field.type.name(), null);
        var limit = input.optInt(Field.limit.name(), 10);

        List<Episode> episodes;
        if (typeString != null) {
            var type = MemoryType.fromString(typeString);
            episodes = store.byType(type);
        } else {
            episodes = store.recent(limit);
        }

        if (episodes.isEmpty()) {
            return "No memories found.";
        }

        var result = new StringBuilder();
        for (var episode : episodes) {
            result.append("[%s] %s".formatted(episode.timestamp(), episode.content()));
            if (episode.type() != null) {
                result.append(" (type: %s)".formatted(episode.type().name()));
            }
            result.append("\n");
        }
        return result.toString().strip();
    }
}
