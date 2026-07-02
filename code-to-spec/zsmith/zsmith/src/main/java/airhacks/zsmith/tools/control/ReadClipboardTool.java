package airhacks.zsmith.tools.control;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public interface ReadClipboardTool {

    static ToolHandler create() {
        return ToolHandler.of(
                "read_clipboard",
                "Reads the current text content from the system clipboard",
                ToolHandler.emptySchema(),
                input -> read());
    }

    private static String read() {
        try {
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            var contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
            return "Clipboard is empty or does not contain text";
        } catch (Exception e) {
            return "Error reading clipboard: " + e.getMessage();
        }
    }
}
