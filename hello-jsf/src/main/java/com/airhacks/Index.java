package com.airhacks;

import javax.annotation.PostConstruct;
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

    private int age;

    @Inject
    BetriebService service;

    @PostConstruct
    public void init() {
        this.age = 42;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Object save() {
        System.out.println("Clicked ");
        service.save();
        return null;
    }

}
