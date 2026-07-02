import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Comparator;

import org.json.JSONObject;

import airhacks.zsmith.tools.control.ExecuteScriptTool;

void main() throws IOException {
    var tool = ExecuteScriptTool.create();
    var tempDir = Files.createTempDirectory("zunit-execscript");
    try {
        // tool definition
        assert "execute_script".equals(tool.toolName()) : "expected 'execute_script' but got: " + tool.toolName();
        assert !tool.description().isBlank() : "description should not be blank";
        assert tool.inputSchema().has("properties") : "inputSchema should have properties";

        // execute successful script
        var hello = createScript(tempDir, "hello.sh", "#!/bin/sh\necho hello world");
        var helloResult = tool.execute(input(hello));
        assert "hello world".equals(helloResult) : "expected 'hello world' but got: " + helloResult;

        // missing script
        var missingResult = tool.execute(input(Path.of("/nonexistent/script.sh")));
        assert missingResult.contains("Script not found") : "expected 'Script not found' but got: " + missingResult;

        // non-executable script
        var noperm = tempDir.resolve("noperm.sh");
        Files.writeString(noperm, "#!/bin/sh\necho hello");
        Files.setPosixFilePermissions(noperm, PosixFilePermissions.fromString("rw-r--r--"));
        var nopermResult = tool.execute(input(noperm));
        assert nopermResult.contains("Script not executable") : "expected 'Script not executable' but got: " + nopermResult;

        // non-zero exit code
        var fail = createScript(tempDir, "fail.sh", "#!/bin/sh\necho partial\nexit 1");
        var failResult = tool.execute(input(fail));
        assert failResult.contains("partial") : "expected 'partial' in output but got: " + failResult;
        assert failResult.contains("exited with code 1") : "expected 'exited with code 1' but got: " + failResult;
    } finally {
        try (var walk = Files.walk(tempDir)) {
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
        }
    }
}

static Path createScript(Path dir, String name, String content) throws IOException {
    var script = dir.resolve(name);
    Files.writeString(script, content);
    Files.setPosixFilePermissions(script, PosixFilePermissions.fromString("rwxr-xr-x"));
    return script;
}

static JSONObject input(Path scriptPath) {
    return new JSONObject().put("path", scriptPath.toString());
}
