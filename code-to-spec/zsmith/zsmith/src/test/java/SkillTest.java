import java.nio.file.Path;

import airhacks.zsmith.skills.entity.Skill;

void main() {
    // create skill with all fields
    var skill = new Skill("rest-resource", "Creates a JAX-RS resource", "Generate a REST resource...", Path.of("skills/rest-resource/SKILL.md"));
    assert "rest-resource".equals(skill.name()) : "expected 'rest-resource' but got " + skill.name();
    assert "Creates a JAX-RS resource".equals(skill.description()) : "expected 'Creates a JAX-RS resource' but got " + skill.description();
    assert "Generate a REST resource...".equals(skill.content()) : "expected 'Generate a REST resource...' but got " + skill.content();

    // description falls back to first line of content
    var fallback = new Skill("test", null, "First line of content\nSecond line", Path.of("test"));
    assert "First line of content".equals(fallback.description()) : "expected 'First line of content' but got " + fallback.description();

    // blank name throws
    try {
        new Skill("", "desc", "content", Path.of("test"));
        throw new AssertionError("expected IllegalArgumentException for blank name");
    } catch (IllegalArgumentException expected) {
    }

    // blank content throws
    try {
        new Skill("test", "desc", "", Path.of("test"));
        throw new AssertionError("expected IllegalArgumentException for blank content");
    } catch (IllegalArgumentException expected) {
    }

    // catalog entry format
    var deploy = new Skill("deploy", "Deploy the application", "content here", Path.of("test"));
    var entry = deploy.catalogEntry();
    assert "- deploy: Deploy the application".equals(entry) : "expected '- deploy: Deploy the application' but got " + entry;
}
