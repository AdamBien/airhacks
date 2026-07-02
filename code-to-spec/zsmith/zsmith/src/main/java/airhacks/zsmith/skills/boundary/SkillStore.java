package airhacks.zsmith.skills.boundary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.skills.control.SkillParser;
import airhacks.zsmith.skills.entity.Skill;
import airhacks.zsmith.skills.entity.SkillLoadEvent;

public class SkillStore {

    static final String SKILL_FILE = "SKILL.md";
    static final String SKILLS_DIR = "skills";

    private final Map<String, Skill> skills;

    public SkillStore(List<Path> searchDirectories) {
        this.skills = new LinkedHashMap<>();
        for (var dir : searchDirectories) {
            scanDirectory(dir);
        }
        Log.info("skills loaded: " + this.skills.size());
    }

    SkillStore(Map<String, Skill> skills) {
        this.skills = new LinkedHashMap<>(skills);
    }

    public SkillStore filtered(Set<String> names) {
        var retained = new LinkedHashMap<String, Skill>();
        for (var raw : names) {
            var name = normalizeName(raw);
            var skill = this.skills.get(name);
            if (skill == null) {
                Log.warning("skill not found, skipped from filter: " + raw);
                continue;
            }
            retained.put(name, skill);
        }
        return new SkillStore(retained);
    }

    static String normalizeName(String raw) {
        return raw == null ? "" : raw.trim().replace("/", "");
    }

    public static SkillStore forAgent(String agentName) {
        var userHome = System.getProperty("user.home");
        var dirs = List.of(
            Path.of(userHome, "." + ZCfg.APP_NAME, SKILLS_DIR),
            Path.of(userHome, "." + ZCfg.APP_NAME, agentName, SKILLS_DIR),
            Path.of(SKILLS_DIR),
            Path.of(agentName, SKILLS_DIR)
        );
        return new SkillStore(dirs);
    }

    void scanDirectory(Path directory) {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (var entries = Files.list(directory)) {
            entries.filter(Files::isDirectory)
                   .forEach(this::loadSkillFromDirectory);
        } catch (IOException e) {
            Log.warning("could not scan skills directory " + directory + ": " + e.getMessage());
        }
    }

    void loadSkillFromDirectory(Path skillDir) {
        var skillFile = skillDir.resolve(SKILL_FILE);
        if (!Files.isRegularFile(skillFile)) {
            return;
        }
        var event = new SkillLoadEvent();
        event.path = skillFile.toString();
        event.begin();
        try {
            var raw = Files.readString(skillFile);
            var skill = SkillParser.parse(raw, skillDir.getFileName().toString(), skillFile);
            this.skills.put(skill.name(), skill);
            event.skillName = skill.name();
            event.contentSize = skill.content().length();
            event.outcome = "loaded";
        } catch (IOException e) {
            Log.warning("could not read skill file " + skillFile + ": " + e.getMessage());
            event.outcome = "io_error";
        } catch (IllegalArgumentException e) {
            Log.warning("invalid skill file " + skillFile + ": " + e.getMessage());
            event.outcome = "parse_error";
        } finally {
            if (event.shouldCommit()) {
                event.commit();
            }
        }
    }

    public Skill load(String name) {
        return this.skills.get(name);
    }

    public List<Skill> allSkills() {
        return List.copyOf(this.skills.values());
    }

    public String catalog() {
        if (this.skills.isEmpty()) {
            return "";
        }
        return this.skills.values().stream()
                .map(Skill::catalogEntry)
                .collect(Collectors.joining("\n", "## Available Skills\n\n", "\n\nUse the load_skill tool to activate a skill by name before applying it."));
    }
}
