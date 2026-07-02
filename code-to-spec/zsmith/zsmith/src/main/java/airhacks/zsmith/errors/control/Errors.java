package airhacks.zsmith.errors.control;

public interface Errors {

    static String summarize(Throwable t) {
        var root = rootCause(t);
        var rootClass = root.getClass().getSimpleName();
        var rootMessage = root.getMessage();
        var topMessage = t.getMessage();
        if (topMessage == null) {
            return rootMessage != null ? "%s: %s".formatted(rootClass, rootMessage) : rootClass;
        }
        if (rootMessage != null && !rootMessage.equals(topMessage)) {
            return "%s (%s: %s)".formatted(topMessage, rootClass, rootMessage);
        }
        return "%s (%s)".formatted(topMessage, rootClass);
    }

    static Throwable rootCause(Throwable t) {
        var cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
