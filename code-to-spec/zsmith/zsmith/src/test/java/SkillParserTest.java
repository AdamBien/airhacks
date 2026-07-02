import java.nio.file.Path;

import airhacks.zsmith.skills.control.SkillParser;

void main() {
    parsesFrontmatter();
    fallsBackToProvidedNameWhenFrontmatterMissingName();
    treatsContentWithoutFrontmatterAsRaw();
    handlesUnclosedFrontmatterAsRaw();
    ignoresUnknownFrontmatterKeys();
    stripsWhitespaceAroundValues();
}

void parsesFrontmatter() {
    var raw = """
            ---
            name: rest-resource
            description: Creates a JAX-RS resource
            ---
            Generate a REST resource.
            """;
    var skill = SkillParser.parse(raw, "fallback", Path.of("skill.md"));
    assert "rest-resource".equals(skill.name()) : "expected 'rest-resource' but got " + skill.name();
    assert "Creates a JAX-RS resource".equals(skill.description()) : "expected description mismatch, got " + skill.description();
    assert "Generate a REST resource.".equals(skill.content()) : "expected content mismatch, got " + skill.content();
}

void fallsBackToProvidedNameWhenFrontmatterMissingName() {
    var raw = """
            ---
            description: A skill without a name field
            ---
            Some content.
            """;
    var skill = SkillParser.parse(raw, "dir-name", Path.of("skill.md"));
    assert "dir-name".equals(skill.name()) : "expected 'dir-name' but got " + skill.name();
    assert "A skill without a name field".equals(skill.description()) : "expected description mismatch, got " + skill.description();
}

void treatsContentWithoutFrontmatterAsRaw() {
    var skill = SkillParser.parse("Just plain content here.", "my-skill", Path.of("skill.md"));
    assert "my-skill".equals(skill.name()) : "expected 'my-skill' but got " + skill.name();
    assert "Just plain content here.".equals(skill.content()) : "expected content mismatch, got " + skill.content();
}

void handlesUnclosedFrontmatterAsRaw() {
    var raw = "---\nname: broken\ncontent body without closing delimiter";
    var skill = SkillParser.parse(raw, "fallback", Path.of("skill.md"));
    assert "fallback".equals(skill.name()) : "expected 'fallback' but got " + skill.name();
    assert raw.equals(skill.content()) : "expected raw passthrough, got " + skill.content();
}

void ignoresUnknownFrontmatterKeys() {
    var raw = """
            ---
            name: keeper
            random: value
            description: kept
            ---
            body
            """;
    var skill = SkillParser.parse(raw, "fallback", Path.of("skill.md"));
    assert "keeper".equals(skill.name()) : "expected 'keeper' but got " + skill.name();
    assert "kept".equals(skill.description()) : "expected 'kept' but got " + skill.description();
}

void stripsWhitespaceAroundValues() {
    var raw = """
            ---
            name:   spaced-name
            description:    spaced description
            ---
            body
            """;
    var skill = SkillParser.parse(raw, "fallback", Path.of("skill.md"));
    assert "spaced-name".equals(skill.name()) : "expected 'spaced-name' but got " + skill.name();
    assert "spaced description".equals(skill.description()) : "expected 'spaced description' but got " + skill.description();
}
