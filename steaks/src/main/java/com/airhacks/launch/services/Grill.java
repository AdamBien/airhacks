package com.airhacks.launch.services;

/**
 *
 * @author airhacks.com
 */
public class Grill {

    private String name;

    public Grill(String name) {
        this.name = name;
    }

    public String boot() {
        return "fire " + name;
    }

}
