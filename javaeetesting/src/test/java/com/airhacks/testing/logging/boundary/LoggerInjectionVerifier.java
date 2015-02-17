package com.airhacks.testing.logging.boundary;

import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class LoggerInjectionVerifier {

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

}
