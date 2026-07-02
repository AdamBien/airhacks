package airhacks.zsmith.tools.control;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import org.json.JSONObject;

public interface WriteClipboardTool {

    enum Field { text }

    static ToolHandler create() {
        return ToolHandler.of(
                "write_clipboard",
                "Writes text content to the system clipboard",
                ToolHandler.schema(ToolHandler.Prop.string(Field.text, "The text to write to the clipboard")),
                WriteClipboardTool::run);
    }

    private static String run(JSONObject input) {
        try {
            var text = input.getString(Field.text.name());
            var selection = new StringSelection(text);
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            return "Text written to clipboard";
        } catch (Exception e) {
            return "Error writing to clipboard: " + e.getMessage();
        }
    }
}
