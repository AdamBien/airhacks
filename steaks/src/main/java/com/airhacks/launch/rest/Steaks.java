package com.airhacks.launch.rest;

import com.airhacks.launch.entities.Steak;
import com.airhacks.launch.services.SteakService;
import java.net.URI;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
//try with @RequestScoped
//curl -XPOST -H'Content-type: application/json' -d'{"id":0,"weight":45}' http://localhost:8080/steaks/resources/steaks
@Stateless
@Path("steaks")
public class Steaks {

    @Inject
    SteakService service;

    @PersistenceContext
    EntityManager em;

    @Resource
    SessionContext cs;

    @GET
    public List<Steak> all() {
        System.out.println("--  " + service.getClass().getName());
        return service.steaks();
    }

    @OPTIONS
    public Steak sample() {
        return new Steak(42);
    }

    @GET
    @Path("{id}")
    public Steak find(@PathParam("id") long id) {
        Steak found = this.service.find(id);
        em.clear();
        found.grillMe();
        return found;
    }

    @POST
    public Response save(JsonObject steak, @Context UriInfo info) {
        Steak notGrilled = this.service.save(new Steak(steak.getInt("weight")));
        notGrilled.grillMe();
        this.cs.setRollbackOnly();
        URI location = info.getAbsolutePathBuilder().path("/" + notGrilled.getId()).build();
        return Response.created(location).build();
    }

}
