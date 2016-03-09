package com.airhacks.launch.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Path("steaks")
public class Steaks {

    @GET
    public JsonObject all() {
        return Json.createObjectBuilder().
                add("type", "medium").
                build();
    }

}
