package com.airhacks.launch.services;

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Interceptors(Audit.class)
public class Grill {

    @PostConstruct
    public void init() {
        System.out.println("-- grill created");
    }

    public String boot() {
        return "fire";
    }

}
