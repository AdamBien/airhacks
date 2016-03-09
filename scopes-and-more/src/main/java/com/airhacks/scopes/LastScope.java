package com.airhacks.scopes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class LastScope {

    @PostConstruct
    public void init() {
        System.out.println("LastScope");
    }

    public String last() {
        return "last duke " + System.currentTimeMillis();
    }

}
