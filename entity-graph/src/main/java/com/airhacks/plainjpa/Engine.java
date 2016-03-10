package com.airhacks.plainjpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author adam-bien.com
 */
@Entity
public class Engine {

    @Id
    @GeneratedValue
    private long id;
    private long power;

    public Engine() {
    }

    public Engine(long power) {
        this.power = power;
    }

}
