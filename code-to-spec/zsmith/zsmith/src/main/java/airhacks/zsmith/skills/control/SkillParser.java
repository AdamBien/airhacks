package airhacks.zsmith.skills.control;

import java.nio.file.Path;

import airhacks.zsmith.skills.entity.Skill;

public interface SkillParser {

    String DELIMITER = "---";

    static Skill parse(String raw, String fallbackName, Path path) {
        String name = fallbackName;
        String description = null;
        String content;

        if (raw.startsWith(DELIMITER)) {
            var endIndex = raw.indexOf(DELIMITER, DELIMITER.length());
            if (endIndex > DELIMITER.length()) {
                var frontmatter = raw.substring(DELIMITER.length(), endIndex).strip();
                content = raw.substring(endIndex + DELIMITER.length()).strip();

                for (var line : frontmatter.lines().toList()) {
                    var colonIndex = line.indexOf(':');
                    if (colonIndex <= 0) continue;
                    var key = line.substring(0, colonIndex).strip();
                    var value = line.substring(colonIndex + 1).strip();
                    switch (key) {
                        case "name" -> name = value;
                        case "description" -> description = value;
                    }
                }
            } else {
                content = raw;
            }
        } else {
            content = raw;
        }

        return new Skill(name, description, content, path);
    }
}
