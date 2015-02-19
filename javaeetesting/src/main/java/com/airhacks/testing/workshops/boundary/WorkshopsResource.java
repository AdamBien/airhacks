package com.airhacks.testing.workshops.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("workshops")
public class WorkshopsResource {

    @Inject
    WorkshopsCatalog catalog;

    @Inject
    Validator validator;

    @GET
    public Response expose(@Context UriInfo info) {
        return Response.ok(Json.createObjectBuilder().
                add("workshops", catalog.all()).
                build()).header("x-info", "I like rest").
                build();
    }

    @POST
    public void newWorkshop(JsonObject workshop) {
    }

}
