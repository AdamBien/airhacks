import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.boundary.Tools;
import airhacks.zsmith.tools.control.CalculatorTool;
import airhacks.zsmith.tools.control.LinkCheckerTool;

void main() {
    // LinkCheckerTool can be registered via withTool()
    var agent = new Agent().withSystemPrompt("You are a helpful assistant.")
            .withTool(LinkCheckerTool.create());
    assert agent.tools().containsKey("check_link") : "agent should contain 'check_link' tool";

    // can be registered alongside other tools
    var multi = new Agent().withSystemPrompt("You are a helpful assistant.")
            .withTool(CalculatorTool.create())
            .withTool(LinkCheckerTool.create());
    assert multi.tools().containsKey("check_link") : "agent should contain 'check_link' tool";
    assert multi.tools().containsKey("calculator") : "agent should contain 'calculator' tool";
    assert multi.tools().size() >= 2 : "agent should have at least 2 tools but got " + multi.tools().size();

    // Tools enum constants work
    var enumAgent = new Agent().withSystemPrompt("You are a helpful assistant.")
            .withTools(Tools.CALCULATOR, Tools.LINK_CHECKER);
    assert enumAgent.tools().containsKey("check_link") : "agent should contain 'check_link' via enum";
    assert enumAgent.tools().containsKey("calculator") : "agent should contain 'calculator' via enum";
    assert enumAgent.tools().size() >= 2 : "agent should have at least 2 tools but got " + enumAgent.tools().size();
}
