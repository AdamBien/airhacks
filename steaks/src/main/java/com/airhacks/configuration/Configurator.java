package com.airhacks.configuration;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author airhacks.com
 */
public class Configurator {

    @Produces
    public String configure(InjectionPoint ip) {
        String key = ip.getMember().getDeclaringClass().getName() + " --> " + ip.getMember().getName();
        return "configured weber " + key;
    }
}
