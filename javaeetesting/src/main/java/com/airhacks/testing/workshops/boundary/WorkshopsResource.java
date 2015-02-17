package com.airhacks.testing.workshops.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("workshops")
public class WorkshopsResource {

    @Inject
    WorkshopsCatalog catalog;

    @GET
    public JsonObject expose() {
        return Json.createObjectBuilder().
                add("workshops", catalog.all()).
                build();
    }

}
