package com.airhacks;

/**
 *
 * @author airhacks.com
 */
public class Betrieb {

    private String id;

    private Maschine maschine;

    public Betrieb(String id) {
        this.id = id;
        this.maschine = new Maschine((int) System.currentTimeMillis());
    }

    public Maschine getMaschine() {
        return maschine;
    }

    @Override
    public String toString() {
        return "Betrieb{" + "id=" + id + ", maschine=" + maschine + '}';
    }

}
