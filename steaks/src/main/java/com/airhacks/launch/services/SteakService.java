package com.airhacks.launch.services;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Interceptors(Audit.class)
public class SteakService {

    public SteakService() {
        System.out.println("--- don't use constructors");
    }

    @PostConstruct
    public void initialize() {
        System.out.println("-- fully initialized");
    }

    public String steaks() {
        return "bloody";
    }

}
