package airhacks.zsmith.skills.entity;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("airhacks.zsmith.skills.Load")
@Label("Skill Load")
@Category({"zsmith", "skills"})
@Description("Single skill read from disk during SkillStore initialization")
public class SkillLoadEvent extends Event {

    @Label("Skill Name")
    public String skillName;

    @Label("Path")
    public String path;

    @Label("Content Size")
    public int contentSize;

    @Label("Outcome")
    public String outcome;
}
