package airhacks.ebank.leasing.boundary;

import org.eclipse.microprofile.metrics.annotation.Timed;

import airhacks.ebank.Boundary;
import airhacks.ebank.leasing.entity.Lease;
import airhacks.ebank.logging.control.EBLog;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Boundary
@Path("leases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeasesResource {

    @PersistenceContext
    EntityManager em;

    @Inject
    EBLog log;

    @POST
    @Timed
    public Response create(Lease lease) {
        this.log.info("create lease " + lease);
        this.em.persist(lease);
        return Response
                .status(Response.Status.CREATED)
                .entity(lease)
                .build();
    }

    @GET
    @Path("{id}")
    @Timed
    public Response lease(@PathParam("id") Long id) {
        this.log.info("get lease " + id);
        var lease = this.em.find(Lease.class, id);
        if (lease == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(lease).build();
    }
}
