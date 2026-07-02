package airhacks.zsmith.skills.entity;

import java.nio.file.Path;

public record Skill(String name, String description, String content, Path path) {

    public Skill {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Skill content must not be empty");
        }
        if (description == null || description.isBlank()) {
            description = content.lines().findFirst().orElse(name);
        }
    }

    public String catalogEntry() {
        return "- %s: %s".formatted(this.name, this.description);
    }
}
