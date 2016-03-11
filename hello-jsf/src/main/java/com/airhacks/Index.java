package com.airhacks;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.Size;

/**
 *
 * @author airhacks.com
 */
@Model
public class Index {

    @Size(min = 3, max = 5)
    private String name;

    @Inject
    BetriebService service;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object save() {
        System.out.println("Clicked ");
        service.save();
        return null;
    }

}
