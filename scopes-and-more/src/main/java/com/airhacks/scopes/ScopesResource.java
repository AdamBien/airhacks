package com.airhacks.scopes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
@Path("scopes")
public class ScopesResource {

    @Inject
    ScopedState state;

    @PostConstruct
    public void init() {
        System.out.println("--ScopesResources");
    }

    @GET
    public String scope() {
        return state.getMessage();
    }

}
