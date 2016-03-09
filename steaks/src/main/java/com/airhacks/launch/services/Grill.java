package com.airhacks.launch.services;

import javax.enterprise.inject.Alternative;

/**
 *
 * @author airhacks.com
 */
@Alternative
public class Grill {

    private String name;

    public Grill() {

    }

    public Grill(String name) {
        this.name = name;
    }

    public String boot() {
        return "fire " + name;
    }

}
