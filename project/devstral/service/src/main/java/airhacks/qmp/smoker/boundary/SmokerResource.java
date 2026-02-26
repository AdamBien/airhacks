package airhacks.qmp.smoker.boundary;

import airhacks.qmp.smoker.control.SmokerService;
import airhacks.qmp.smoker.entity.Smoker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("smokers")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SmokerResource {

    @Inject
    SmokerService smokerService;

    @GET
    public List<Smoker> getAllSmokers() {
        return smokerService.getAllSmokers();
    }

    @GET
    @Path("{name}")
    public Smoker getSmoker(@PathParam("name") String name) {
        return smokerService.getSmokerByName(name);
    }

    @POST
    public Response addSmoker(Smoker smoker) {
        Smoker created = smokerService.addSmoker(smoker);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("{name}")
    public Response deleteSmoker(@PathParam("name") String name) {
        smokerService.deleteSmoker(name);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("{name}/analysis")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSmokingAnalysis(@PathParam("name") String name) {
        return smokerService.getSmokingAnalysis(name);
    }
}