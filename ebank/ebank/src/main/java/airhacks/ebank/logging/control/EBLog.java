package airhacks.ebank.logging.control;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * Adapter for System.Logger that provides a convenient, domain-specific logging API.
 * Encapsulates the System.Logger, providing and simplified API.
 */
public record EBLog(Logger systemLogger) {

    public EBLog(Class<?> clazz){
        this(System.getLogger(clazz.getName()));
    }

    public void info(String message){
        systemLogger.log(Level.INFO, message);
    }

    public void error(String message){
        systemLogger.log(Level.ERROR, message);
    }

    public void error(String message, Throwable exception){
        systemLogger.log(Level.ERROR, message, exception);
    }
}
