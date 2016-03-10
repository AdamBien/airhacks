package com.airhacks.plainjpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

/**
 *
 * @author adam-bien.com
 */
@Entity
@NamedEntityGraph(name = "withEngines", attributeNodes = {
    @NamedAttributeNode("engines")})
public class Spaceship {

    @Id
    private String name;
    private int speed;

    @OneToMany
    private List<Engine> engines;

    public Spaceship() {
        this.engines = new ArrayList<>();
    }

    public Spaceship(String name, int speed) {
        this();
        this.engines.add(new Engine(42));
        this.engines.add(new Engine(21));
        this.name = name;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
