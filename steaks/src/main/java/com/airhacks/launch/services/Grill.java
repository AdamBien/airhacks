package com.airhacks.launch.services;

import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class Grill {

    @Inject
    private String name;

    public String boot() {
        return "fire " + name;
    }

}
