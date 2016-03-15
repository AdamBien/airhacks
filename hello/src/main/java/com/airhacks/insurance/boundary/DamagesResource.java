package com.airhacks.insurance.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("damages")
public class DamagesResource {

    @Inject
    DamageService service;

    @GET
    @Path("{id}")
    public String damage(@PathParam("id") long id) {
        return service.getDamage(id);
    }

}
