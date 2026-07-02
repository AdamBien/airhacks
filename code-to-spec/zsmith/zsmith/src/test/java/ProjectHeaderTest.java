import airhacks.zsmith.claude.control.Claude;
import airhacks.zsmith.claude.control.Claude.Wire;
import airhacks.zsmith.configuration.control.ZCfg;

/// Verifies that the project/workspace header is selected by wire format and sourced with the
/// right precedence — wire-native key first, shared `bedrock.project.id` as fallback — so that
/// Bedrock Mantle's Messages and Chat Completions routes each receive the header they accept.
void main() {
    clearProps();

    anthropicWireUsesWorkspaceHeader();
    openaiWireUsesProjectHeader();
    bedrockProjectIdFallsBackForAnthropicWire();
    bedrockProjectIdFallsBackForOpenAIWire();
    wireNativeKeyWinsOverBedrockProjectId();
    blankValueProducesNoHeader();
    unsetProducesNoHeader();

    System.out.println("ProjectHeaderTest passed");
}

void anthropicWireUsesWorkspaceHeader() {
    reload(props -> props.setProperty("anthropic.workspace.id", "ws_123"));
    var header = Claude.projectHeader(Wire.ANTHROPIC).orElseThrow();
    assert "anthropic-workspace-id".equals(header.name()) : "wrong header name: " + header.name();
    assert "ws_123".equals(header.value()) : "wrong value: " + header.value();
}

void openaiWireUsesProjectHeader() {
    reload(props -> props.setProperty("openai.project", "proj_456"));
    var header = Claude.projectHeader(Wire.OPENAI).orElseThrow();
    assert "openai-project".equals(header.name()) : "wrong header name: " + header.name();
    assert "proj_456".equals(header.value()) : "wrong value: " + header.value();
}

void bedrockProjectIdFallsBackForAnthropicWire() {
    reload(props -> props.setProperty("bedrock.project.id", "bp_789"));
    var header = Claude.projectHeader(Wire.ANTHROPIC).orElseThrow();
    assert "anthropic-workspace-id".equals(header.name()) : "wrong header name: " + header.name();
    assert "bp_789".equals(header.value()) : "fallback not applied: " + header.value();
}

void bedrockProjectIdFallsBackForOpenAIWire() {
    reload(props -> props.setProperty("bedrock.project.id", "bp_789"));
    var header = Claude.projectHeader(Wire.OPENAI).orElseThrow();
    assert "openai-project".equals(header.name()) : "wrong header name: " + header.name();
    assert "bp_789".equals(header.value()) : "fallback not applied: " + header.value();
}

void wireNativeKeyWinsOverBedrockProjectId() {
    reload(props -> {
        props.setProperty("openai.project", "native_wins");
        props.setProperty("bedrock.project.id", "shared_fallback");
    });
    var header = Claude.projectHeader(Wire.OPENAI).orElseThrow();
    assert "native_wins".equals(header.value()) : "wire-native key should win: " + header.value();
}

void blankValueProducesNoHeader() {
    reload(props -> props.setProperty("anthropic.workspace.id", "   "));
    assert Claude.projectHeader(Wire.ANTHROPIC).isEmpty() : "blank value must not yield a header";
}

void unsetProducesNoHeader() {
    reload(_ -> {});
    assert Claude.projectHeader(Wire.ANTHROPIC).isEmpty() : "unset must not yield a header";
    assert Claude.projectHeader(Wire.OPENAI).isEmpty() : "unset must not yield a header";
}

/// Repopulates ZCfg from system properties only — the relevant keys are cleared first so each
/// case starts from a known-empty baseline, then the mutator sets just what it needs.
void reload(java.util.function.Consumer<java.util.Properties> mutator) {
    clearProps();
    mutator.accept(System.getProperties());
    ZCfg.loadBaseConfig("zsmith-test-project-header-" + ProcessHandle.current().pid());
}

void clearProps() {
    System.clearProperty("anthropic.workspace.id");
    System.clearProperty("openai.project");
    System.clearProperty("bedrock.project.id");
}
