package com.airhacks.launch.services;

import com.airhacks.configuration.ConfigurableKey;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class Grill {

    @Inject
    @ConfigurableKey("grill-name")
    private String name;

    public String boot() {
        return "fire " + name;
    }

}
