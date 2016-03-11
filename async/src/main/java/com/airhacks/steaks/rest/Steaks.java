package com.airhacks.steaks.rest;

import com.airhacks.steaks.services.SteakService;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 *
 * @author airhacks.com
 */
@Path("steaks")
public class Steaks {

    @Resource
    ManagedExecutorService mes;

    @Inject
    SteakService service;

    @GET
    public void steak(@Suspended AsyncResponse response) {
        supplyAsync(this.service::steak, mes).
                exceptionally(Throwable::toString).
                thenAccept(response::resume);
    }

}
