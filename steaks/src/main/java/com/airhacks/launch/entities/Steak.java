package com.airhacks.launch.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author airhacks.com
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Steak.findAll, query = "SELECT s FROM Steak s")
public class Steak {

    @Id
    @GeneratedValue
    private long id;

    private int weight;

    private final static String PREFIX = "com.airhacks.launch.entities.Steak.";
    public final static String findAll = PREFIX + "findAll";

    Steak() {
    }

    public Steak(int weight) {
        this.weight = weight;
    }

    public void grillMe() {
        System.out.println("Grilled!");
    }

}
