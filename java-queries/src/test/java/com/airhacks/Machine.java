package com.airhacks;

/**
 *
 * @author airhacks.com
 */
public class Machine {

    private String name;
    private int power;

    public Machine(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

}
