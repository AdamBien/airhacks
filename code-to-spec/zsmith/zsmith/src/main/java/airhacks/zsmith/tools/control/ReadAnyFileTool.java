package airhacks.zsmith.tools.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

public interface ReadAnyFileTool {

    enum Field { path }

    static ToolHandler create() {
        return ToolHandler.of(
                "read_any_file",
                "Reads a file from any location on the filesystem",
                ToolHandler.schema(ToolHandler.Prop.string(Field.path, "Absolute path to the file to read")),
                ReadAnyFileTool::run);
    }

    private static String run(JSONObject input) {
        if (!input.has(Field.path.name())) {
            return "Error: Missing required parameter: path";
        }
        var path = Path.of(input.getString(Field.path.name()));
        if (!Files.exists(path)) {
            return "Error: File not found: " + path;
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return "Error: Could not read file: " + e.getMessage();
        }
    }
}
