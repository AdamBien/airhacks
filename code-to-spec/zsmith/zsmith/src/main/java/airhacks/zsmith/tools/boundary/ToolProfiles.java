package airhacks.zsmith.tools.boundary;

import java.util.List;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.tools.control.ListFilesTool;
import airhacks.zsmith.tools.control.ReadAnyFileTool;
import airhacks.zsmith.tools.control.ReadFileTool;
import airhacks.zsmith.tools.control.ToolHandler;
import airhacks.zsmith.tools.control.WriteAnyFileTool;
import airhacks.zsmith.tools.control.WriteFileTool;

/**
 * Predefined groupings of {@link ToolHandler}s for common agent capabilities.
 *
 * <p>Separates the concern of <em>which tools exist</em> ({@link Tools}) from
 * <em>which tools belong together for a given use case</em>. Agents compose
 * capabilities by selecting a profile rather than cherry-picking individual
 * tools, keeping the wiring in {@link airhacks.zsmith.agent.boundary.Agent}
 * intention-revealing (e.g. {@code agent.withTools(ToolProfiles.userIO())}).
 *
 * <p>Implemented as an interface with constant fields so it acts as a
 * pure namespace — no instantiation, no state, just curated lists.
 */
public interface ToolProfiles {

    static List<ToolHandler> userIO() {
        return List.of(Tools.USER_MESSAGE, Tools.USER_QUESTION, Tools.USER_CONFIRMATION);
    }

    static List<ToolHandler> clipboard() {
        return List.of(Tools.READ_CLIPBOARD, Tools.WRITE_CLIPBOARD);
    }

    static List<ToolHandler> fileIO(String agentName) {
        var sandbox = new SandboxedFileSystem(ZCfg.sandboxPath(agentName));
        return List.of(ReadFileTool.create(sandbox), WriteFileTool.create(sandbox), ListFilesTool.create(sandbox),
                ReadAnyFileTool.create(), WriteAnyFileTool.create());
    }

    static List<ToolHandler> all() {
        return List.of(Tools.values());
    }
}
