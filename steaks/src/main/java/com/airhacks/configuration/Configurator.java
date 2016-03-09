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
        ConfigurableKey annotation = ip.getAnnotated().getAnnotation(ConfigurableKey.class);
        return "configured weber " + annotation.value();
    }
}
