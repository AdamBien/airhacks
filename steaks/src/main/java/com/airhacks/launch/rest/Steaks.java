package com.airhacks.launch.rest;

import com.airhacks.launch.entities.Steak;
import com.airhacks.launch.services.SteakService;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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

    @GET
    public List<Steak> all() {
        System.out.println("--  " + service.getClass().getName());
        return service.steaks();
    }

    @OPTIONS
    public Steak sample() {
        return new Steak(42);
    }

    @POST
    public void save(JsonObject steak) {
        Steak notGrilled = this.service.save(new Steak(steak.getInt("weight")));
        notGrilled.grillMe();
    }

}
