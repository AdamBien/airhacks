package airhacks.zsmith.skills.control;

import org.json.JSONObject;

import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.skills.boundary.SkillStore;
import airhacks.zsmith.tools.control.ToolHandler;

public class LoadSkillTool implements ToolHandler {

    private final SkillStore store;

    public LoadSkillTool(SkillStore store) {
        this.store = store;
    }

    @Override
    public String toolName() {
        return "load_skill";
    }

    @Override
    public String description() {
        return "Loads a skill by name. Returns the full skill content with instructions the assistant should follow to complete the user's request.";
    }

    enum Field { name }

    @Override
    public JSONObject inputSchema() {
        return ToolHandler.schema(Prop.string(Field.name, "The name of the skill to load"));
    }

    @Override
    public String execute(JSONObject input) {
        var name = input.getString(Field.name.name());
        var skill = store.load(name);
        if (skill == null) {
            return "Skill not found: " + name;
        }
        Log.skill(skill.name() + " skill loaded");
        return skill.content();
    }
}
