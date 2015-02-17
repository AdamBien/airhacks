package com.airhacks.testing.logging.boundary;

import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author airhacks.com
 */
public class LoggerProducer {

    @Produces
    public Logger expose(InjectionPoint ip) {
        String className = ip.getMember().getDeclaringClass().getName();
        return Logger.getLogger(className);
    }

}
