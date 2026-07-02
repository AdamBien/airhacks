import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.json.JSONObject;

import airhacks.zsmith.tools.control.WriteAnyFileTool;

void main() throws IOException {
    var tool = WriteAnyFileTool.create();

    assert "write_any_file".equals(tool.toolName()) : "expected 'write_any_file' but got: " + tool.toolName();

    Objects.requireNonNull(tool.description(), "description should not be null");
    assert !tool.description().isBlank() : "description should be non-empty";

    var schema = tool.inputSchema().toString();
    assert schema.contains("\"path\"") : "inputSchema should contain '\"path\"'";
    assert schema.contains("\"content\"") : "inputSchema should contain '\"content\"'";
    assert schema.contains("\"append\"") : "inputSchema should contain '\"append\"'";
    assert schema.contains("\"required\"") : "inputSchema should contain '\"required\"'";

    var missingPath = tool.execute(new JSONObject().put("content", "x"));
    assert "Error: Missing required parameter: path".equals(missingPath)
            : "expected error for missing path but got: " + missingPath;

    var missingContent = tool.execute(new JSONObject().put("path", "/tmp/zsmith-test.txt"));
    assert "Error: Missing required parameter: content".equals(missingContent)
            : "expected error for missing content but got: " + missingContent;

    var relative = tool.execute(new JSONObject().put("path", "relative/path.txt").put("content", "x"));
    assert relative.startsWith("Error: Path must be absolute")
            : "expected absolute-path error but got: " + relative;

    var tempDir = Files.createTempDirectory("write-any-file-test");
    var target = tempDir.resolve("nested/dir/output.txt");

    var write = tool.execute(new JSONObject()
            .put("path", target.toString())
            .put("content", "hello"));
    assert write.startsWith("File written successfully:")
            : "expected success but got: " + write;
    assert "hello".equals(Files.readString(target))
            : "file content mismatch: " + Files.readString(target);

    var overwrite = tool.execute(new JSONObject()
            .put("path", target.toString())
            .put("content", "replaced"));
    assert overwrite.startsWith("File written successfully:")
            : "expected success but got: " + overwrite;
    assert "replaced".equals(Files.readString(target))
            : "expected overwrite but got: " + Files.readString(target);

    var appended = tool.execute(new JSONObject()
            .put("path", target.toString())
            .put("content", "-tail")
            .put("append", "true"));
    assert appended.startsWith("Appended to file:")
            : "expected append message but got: " + appended;
    assert "replaced-tail".equals(Files.readString(target))
            : "expected append result but got: " + Files.readString(target);

    Files.deleteIfExists(target);
    Files.deleteIfExists(target.getParent());
    Files.deleteIfExists(target.getParent().getParent());
    Files.deleteIfExists(tempDir);
}
