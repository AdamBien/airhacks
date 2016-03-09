package com.airhacks.scopes;

import javax.annotation.PostConstruct;

/**
 *
 * @author airhacks.com
 */
public class ScopedState {

    @PostConstruct
    public void init() {
        System.out.println("-- ScopedState");
    }

    public String getMessage() {
        return "hey duke " + System.currentTimeMillis();
    }

}
