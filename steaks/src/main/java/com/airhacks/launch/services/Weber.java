package com.airhacks.launch.services;

import javax.enterprise.inject.Produces;

/**
 *
 * @author airhacks.com
 */
public class Weber {

    @Produces
    private final static Grill grill = new Grill("weber");

}
