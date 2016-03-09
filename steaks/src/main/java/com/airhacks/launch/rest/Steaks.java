package com.airhacks.launch.rest;

import com.airhacks.launch.services.SteakService;
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
@Path("steaks")
public class Steaks {

    @Inject
    SteakService service;

    @GET
    public JsonObject all() {
        return Json.createObjectBuilder().
                add("type", service.steaks()).
                build();
    }

}
