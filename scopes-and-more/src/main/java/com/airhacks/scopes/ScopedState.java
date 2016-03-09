package com.airhacks.scopes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
public class ScopedState {

    @Inject
    LastScope last;

    @Inject
    LastScope veryLast;

    @PostConstruct
    public void init() {
        System.out.println("-- ScopedState");
    }

    public String getMessage() {
        System.out.println("-- same proxy? " + (last == veryLast));
        return "hey duke " + System.currentTimeMillis() + last.last() + " " + veryLast.last();
    }

}
