package com.airhacks.testing.workshops.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author airhacks.com
 */
@Entity
@Table(name = "A_WORKSHOP")
@NamedQuery(name = "all", query = "Select w from Workshop w")
public class Workshop {

    @Id
    private String name;
    private int maxCapacity;

    public Workshop(String name) {
        this.name = name;
    }

    public Workshop() {
    }

}
