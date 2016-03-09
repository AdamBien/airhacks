package com.airhacks.scopes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
public class ScopedState {

    @PostConstruct
    public void init() {
        System.out.println("-- ScopedState");
    }

    public String getMessage() {
        return "hey duke " + System.currentTimeMillis();
    }

}
