package com.airhacks.launch.services;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Interceptors(Audit.class)
public class SteakService {

    @Inject
    Grill grill;

    public SteakService() {
        System.out.println("--- don't use constructors");
    }

    @PostConstruct
    public void initialize() {
        System.out.println("-- fully initialized " + grill.getClass().getName());
    }

    public String steaks() {
        return "bloody on " + grill.boot();
    }

}
