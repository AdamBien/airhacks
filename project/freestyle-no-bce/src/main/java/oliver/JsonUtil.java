package oliver;

import java.util.regex.Pattern;

class JsonUtil {

    static String extract(String json, String key) {
        var pattern = "\"" + key + "\"\\s*:\\s*\"?([^\",}]*)\"?";
        var matcher = Pattern.compile(pattern).matcher(json);
        return matcher.find() ? matcher.group(1).strip() : "";
    }
}
