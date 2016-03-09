package com.airhacks.launch.services;

import javax.enterprise.inject.Produces;

/**
 *
 * @author airhacks.com
 */
public class Weber {

    @Produces
    public Grill expose() {
        return new Grill("super weber");
    }

}
