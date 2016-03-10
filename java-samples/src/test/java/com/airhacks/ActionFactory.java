package com.airhacks;

/**
 *
 * @author airhacks.com
 */
class ActionFactory {

    static Action create() {
        try {
            String clazz = System.getProperty("action", "com.airhacks.DynamiteAction");
            return (Action) Class.forName(clazz).newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

}
