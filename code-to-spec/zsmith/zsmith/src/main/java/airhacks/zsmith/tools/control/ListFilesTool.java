package airhacks.zsmith.tools.control;

import java.nio.file.Path;

import airhacks.zsmith.tools.boundary.SandboxedFileSystem;

public interface ListFilesTool {

    static ToolHandler of(String sandboxPath) {
        return create(new SandboxedFileSystem(Path.of(sandboxPath)));
    }

    static ToolHandler create(SandboxedFileSystem fs) {
        return ToolHandler.of(
                "list_files",
                "Lists all files within the sandbox directory",
                ToolHandler.emptySchema(),
                input -> fs.listFiles());
    }
}
