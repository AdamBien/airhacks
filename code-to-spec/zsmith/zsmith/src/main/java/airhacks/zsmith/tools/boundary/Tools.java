package airhacks.zsmith.tools.boundary;

import org.json.JSONObject;

import airhacks.zsmith.tools.control.CalculatorTool;
import airhacks.zsmith.tools.control.CurrentTimeTool;
import airhacks.zsmith.tools.control.ExecuteScriptTool;
import airhacks.zsmith.tools.control.FetchUrlTool;
import airhacks.zsmith.tools.control.LinkCheckerTool;
import airhacks.zsmith.tools.control.ReadAnyFileTool;
import airhacks.zsmith.tools.control.ReadClipboardTool;
import airhacks.zsmith.tools.control.ToolHandler;
import airhacks.zsmith.tools.control.UserConfirmationTool;
import airhacks.zsmith.tools.control.UserMessageTool;
import airhacks.zsmith.tools.control.UserQuestionTool;
import airhacks.zsmith.tools.control.WriteAnyFileTool;
import airhacks.zsmith.tools.control.WriteClipboardTool;

public enum Tools implements ToolHandler {

    CALCULATOR(CalculatorTool.create()),
    CURRENT_TIME(CurrentTimeTool.create()),
    READ_CLIPBOARD(ReadClipboardTool.create()),
    WRITE_CLIPBOARD(WriteClipboardTool.create()),
    READ_ANY_FILE(ReadAnyFileTool.create()),
    WRITE_ANY_FILE(WriteAnyFileTool.create()),
    LINK_CHECKER(LinkCheckerTool.create()),
    FETCH_URL(FetchUrlTool.create()),
    USER_CONFIRMATION(UserConfirmationTool.create()),
    USER_MESSAGE(UserMessageTool.create()),
    USER_QUESTION(UserQuestionTool.create()),
    EXECUTE_SCRIPT(ExecuteScriptTool.create());

    private final ToolHandler delegate;

    Tools(ToolHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toolName() { return delegate.toolName(); }

    @Override
    public String description() { return delegate.description(); }

    @Override
    public JSONObject inputSchema() { return delegate.inputSchema(); }

    @Override
    public String execute(JSONObject input) { return delegate.execute(input); }

    @Override
    public boolean parallel() { return delegate.parallel(); }
}
