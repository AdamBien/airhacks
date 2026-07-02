import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zsmith.configuration.control.ZCfg;

void main() throws Exception {
    var appName = "zsmith-test-" + ProcessHandle.current().pid();
    ZCfg.loadBaseConfig(appName);

    var agentName = "test-agent";
    ZCfg.storeAgentProperty(agentName, "tools.permissions.calculator", "allow");

    // verify in-memory cache
    assert "allow".equals(ZCfg.string("tools.permissions.calculator"))
            : "expected 'allow' in cache but got: " + ZCfg.string("tools.permissions.calculator");

    // verify persisted to file
    var userHome = System.getProperty("user.home");
    var configFile = Path.of(userHome, "." + appName, agentName, "app.properties");
    assert Files.exists(configFile) : "config file should exist: " + configFile;

    var content = Files.readString(configFile);
    assert content.contains("tools.permissions.calculator=allow")
            : "file should contain permission entry but got: " + content;

    // overwrite with new value
    ZCfg.storeAgentProperty(agentName, "tools.permissions.calculator", "deny");
    assert "deny".equals(ZCfg.string("tools.permissions.calculator"))
            : "expected 'deny' in cache but got: " + ZCfg.string("tools.permissions.calculator");

    var updatedContent = Files.readString(configFile);
    assert updatedContent.contains("tools.permissions.calculator=deny")
            : "file should contain updated entry but got: " + updatedContent;

    // cleanup
    Files.deleteIfExists(configFile);
    Files.deleteIfExists(configFile.getParent());
    Files.deleteIfExists(configFile.getParent().getParent());
}
