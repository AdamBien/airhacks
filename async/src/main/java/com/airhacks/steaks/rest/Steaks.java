package com.airhacks.steaks.rest;

import com.airhacks.steaks.services.SteakService;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
        Consumer<Object> browser = response::resume;
        Supplier<String> supplier = this.service::steak;
        CompletableFuture.supplyAsync(supplier, mes).thenAccept(browser);

    }

}
