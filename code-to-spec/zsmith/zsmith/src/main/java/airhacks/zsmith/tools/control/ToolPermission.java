package airhacks.zsmith.tools.control;

import airhacks.zsmith.configuration.control.ZCfg;

public enum ToolPermission {

    ALLOW,
    DENY,
    CONFIRM;

    public static final String PREFIX = "tools.permissions.";
    static final String DEFAULT_KEY = PREFIX + "default";

    public static ToolPermission resolve(String toolName) {
        var value = ZCfg.string(PREFIX + toolName);
        if (value != null) {
            return parse(value);
        }
        var defaultValue = ZCfg.string(DEFAULT_KEY);
        if (defaultValue != null) {
            return parse(defaultValue);
        }
        return CONFIRM;
    }

    static ToolPermission parse(String value) {
        return valueOf(value.strip().toUpperCase());
    }
}
