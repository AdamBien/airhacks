import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import java.util.Objects;
import java.util.Set;

import airhacks.zsmith.skills.boundary.SkillStore;

void main() throws IOException {
    loadSkillWithFrontmatter();
    loadSkillWithoutFrontmatter();
    loadSkillNameFallsBackToDirectoryName();
    laterDirectoryOverridesEarlier();
    catalogFormatsCorrectly();
    emptyDirectoryProducesEmptyCatalog();
    nonExistentDirectoryIsIgnored();
    loadReturnsNullForUnknownSkill();
    filteredKeepsOnlyNamedSkills();
    filteredIgnoresUnknownNames();
    filteredEmptySetProducesEmptyStore();
}

void loadSkillWithFrontmatter() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var skillDir = tempDir.resolve("rest-resource");
        Files.createDirectories(skillDir);
        Files.writeString(skillDir.resolve("SKILL.md"), """
                ---
                name: rest-resource
                description: Creates a JAX-RS resource
                ---
                Generate a REST resource with proper BCE structure.
                """);

        var store = new SkillStore(List.of(tempDir));
        var skill = Objects.requireNonNull(store.load("rest-resource"), "skill should not be null");
        assert "rest-resource".equals(skill.name()) : "expected 'rest-resource' but got " + skill.name();
        assert "Creates a JAX-RS resource".equals(skill.description()) : "expected 'Creates a JAX-RS resource' but got " + skill.description();
        assert "Generate a REST resource with proper BCE structure.".equals(skill.content()) : "expected content mismatch, got: " + skill.content();
    } finally {
        deleteRecursively(tempDir);
    }
}

void loadSkillWithoutFrontmatter() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var skillDir = tempDir.resolve("my-skill");
        Files.createDirectories(skillDir);
        Files.writeString(skillDir.resolve("SKILL.md"), "Just plain content here.");

        var store = new SkillStore(List.of(tempDir));
        var skill = Objects.requireNonNull(store.load("my-skill"), "skill should not be null");
        assert "my-skill".equals(skill.name()) : "expected 'my-skill' but got " + skill.name();
        assert "Just plain content here.".equals(skill.content()) : "expected 'Just plain content here.' but got " + skill.content();
    } finally {
        deleteRecursively(tempDir);
    }
}

void loadSkillNameFallsBackToDirectoryName() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var skillDir = tempDir.resolve("dir-name");
        Files.createDirectories(skillDir);
        Files.writeString(skillDir.resolve("SKILL.md"), """
                ---
                description: A skill without a name field
                ---
                Some content.
                """);

        var store = new SkillStore(List.of(tempDir));
        var skill = Objects.requireNonNull(store.load("dir-name"), "skill should not be null");
        assert "dir-name".equals(skill.name()) : "expected 'dir-name' but got " + skill.name();
        assert "A skill without a name field".equals(skill.description()) : "expected 'A skill without a name field' but got " + skill.description();
    } finally {
        deleteRecursively(tempDir);
    }
}

void laterDirectoryOverridesEarlier() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var dir1 = tempDir.resolve("dir1");
        var dir2 = tempDir.resolve("dir2");
        var skill1 = dir1.resolve("overlap");
        var skill2 = dir2.resolve("overlap");
        Files.createDirectories(skill1);
        Files.createDirectories(skill2);
        Files.writeString(skill1.resolve("SKILL.md"), """
                ---
                name: overlap
                description: First version
                ---
                Content from dir1.
                """);
        Files.writeString(skill2.resolve("SKILL.md"), """
                ---
                name: overlap
                description: Second version
                ---
                Content from dir2.
                """);

        var store = new SkillStore(List.of(dir1, dir2));
        var skill = store.load("overlap");
        assert "Second version".equals(skill.description()) : "expected 'Second version' but got " + skill.description();
        assert "Content from dir2.".equals(skill.content()) : "expected 'Content from dir2.' but got " + skill.content();
    } finally {
        deleteRecursively(tempDir);
    }
}

void catalogFormatsCorrectly() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var skillDir = tempDir.resolve("test-skill");
        Files.createDirectories(skillDir);
        Files.writeString(skillDir.resolve("SKILL.md"), """
                ---
                name: test-skill
                description: A test skill
                ---
                Content.
                """);

        var store = new SkillStore(List.of(tempDir));
        var catalog = store.catalog();
        assert catalog.contains("## Available Skills") : "catalog should contain '## Available Skills'";
        assert catalog.contains("- test-skill: A test skill") : "catalog should contain '- test-skill: A test skill'";
        assert catalog.contains("load_skill") : "catalog should contain 'load_skill'";
    } finally {
        deleteRecursively(tempDir);
    }
}

void emptyDirectoryProducesEmptyCatalog() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var store = new SkillStore(List.of(tempDir));
        assert "".equals(store.catalog()) : "expected empty catalog but got: " + store.catalog();
    } finally {
        deleteRecursively(tempDir);
    }
}

void nonExistentDirectoryIsIgnored() {
    var store = new SkillStore(List.of(Path.of("/nonexistent/path")));
    assert store.allSkills().isEmpty() : "expected empty skills for non-existent directory";
}

void loadReturnsNullForUnknownSkill() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        var store = new SkillStore(List.of(tempDir));
        assert store.load("nonexistent") == null : "expected null for unknown skill";
    } finally {
        deleteRecursively(tempDir);
    }
}

void filteredKeepsOnlyNamedSkills() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        writeSkill(tempDir, "alpha", "alpha description", "alpha content");
        writeSkill(tempDir, "beta", "beta description", "beta content");
        writeSkill(tempDir, "gamma", "gamma description", "gamma content");

        var store = new SkillStore(List.of(tempDir));
        var filtered = store.filtered(Set.of("alpha", "gamma"));

        assert filtered.allSkills().size() == 2 : "expected 2 skills but got " + filtered.allSkills().size();
        assert filtered.load("alpha") != null : "alpha should be present";
        assert filtered.load("gamma") != null : "gamma should be present";
        assert filtered.load("beta") == null : "beta should have been filtered out";
    } finally {
        deleteRecursively(tempDir);
    }
}

void filteredIgnoresUnknownNames() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        writeSkill(tempDir, "alpha", "alpha description", "alpha content");

        var store = new SkillStore(List.of(tempDir));
        var filtered = store.filtered(Set.of("alpha", "missing"));

        assert filtered.allSkills().size() == 1 : "expected 1 skill but got " + filtered.allSkills().size();
        assert filtered.load("alpha") != null : "alpha should be present";
        assert filtered.load("missing") == null : "missing should not be present";
    } finally {
        deleteRecursively(tempDir);
    }
}

void filteredEmptySetProducesEmptyStore() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-skillstore");
    try {
        writeSkill(tempDir, "alpha", "alpha description", "alpha content");

        var store = new SkillStore(List.of(tempDir));
        var filtered = store.filtered(Set.of());

        assert filtered.allSkills().isEmpty() : "expected empty filtered store";
        assert "".equals(filtered.catalog()) : "expected empty catalog but got: " + filtered.catalog();
    } finally {
        deleteRecursively(tempDir);
    }
}

static void writeSkill(Path baseDir, String name, String description, String content) throws IOException {
    var skillDir = baseDir.resolve(name);
    Files.createDirectories(skillDir);
    Files.writeString(skillDir.resolve("SKILL.md"), """
            ---
            name: %s
            description: %s
            ---
            %s
            """.formatted(name, description, content));
}

static void deleteRecursively(Path path) throws IOException {
    try (var walk = Files.walk(path)) {
        walk.sorted(Comparator.reverseOrder())
            .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
    }
}
