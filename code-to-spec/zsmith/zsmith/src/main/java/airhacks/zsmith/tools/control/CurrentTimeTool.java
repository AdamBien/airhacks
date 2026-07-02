package airhacks.zsmith.tools.control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface CurrentTimeTool {

    DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static ToolHandler create() {
        return ToolHandler.of(
                "current_time",
                "Returns the current date and time",
                ToolHandler.emptySchema(),
                input -> LocalDateTime.now().format(FORMAT));
    }
}
