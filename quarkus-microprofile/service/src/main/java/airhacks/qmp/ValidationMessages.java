package airhacks.qmp;

import java.util.ResourceBundle;

public interface ValidationMessages {

    ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    static String get(String key) {
        return BUNDLE.getString(key);
    }
}
