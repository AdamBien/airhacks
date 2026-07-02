import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import airhacks.zsmith.skills.boundary.SkillStore;
import airhacks.zsmith.skills.control.LoadSkillTool;

void main() throws IOException {
    var tempDir = Files.createTempDirectory("zunit-loadskill");
    try {
        var skillDir = tempDir.resolve("greeting");
        Files.createDirectories(skillDir);
        Files.writeString(skillDir.resolve("SKILL.md"), """
                ---
                name: greeting
                description: Greets the user
                ---
                Always greet the user warmly and ask how you can help.
                """);
        var store = new SkillStore(List.of(tempDir));
        var tool = new LoadSkillTool(store);

        // load existing skill
        var result = tool.execute(new JSONObject().put("name", "greeting"));
        assert "Always greet the user warmly and ask how you can help.".equals(result) : "expected skill content but got: " + result;

        // load non-existent skill
        var missing = tool.execute(new JSONObject().put("name", "nonexistent"));
        assert missing.contains("Skill not found") : "expected 'Skill not found' but got: " + missing;

        // tool definition
        assert "load_skill".equals(tool.toolName()) : "expected 'load_skill' but got: " + tool.toolName();
        assert !tool.description().isBlank() : "description should not be blank";
        assert tool.inputSchema().has("properties") : "inputSchema should have properties";
    } finally {
        try (var walk = Files.walk(tempDir)) {
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
        }
    }
}
