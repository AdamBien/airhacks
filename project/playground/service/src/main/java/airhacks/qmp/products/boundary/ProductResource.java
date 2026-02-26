package airhacks.qmp.products.boundary;

import airhacks.qmp.products.control.ProductService;
import airhacks.qmp.products.entity.CreateProductRequest;
import airhacks.qmp.products.entity.UpdateProductRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("products")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("SELLER")
    public Response create(CreateProductRequest request) {
        var sellerId = this.jwt.getSubject();
        var product = this.productService.create(sellerId, request);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @GET
    @PermitAll
    public Response listActive() {
        var products = this.productService.findActive();
        return Response.ok(products).build();
    }

    @GET
    @Path("{id}")
    @PermitAll
    public Response getById(@PathParam("id") String id) {
        return this.productService.findById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("SELLER")
    public Response update(@PathParam("id") String id, UpdateProductRequest request) {
        var sellerId = this.jwt.getSubject();
        var existing = this.productService.findById(id);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!existing.get().sellerId().equals(sellerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return this.productService.update(id, sellerId, request)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("SELLER")
    public Response delete(@PathParam("id") String id) {
        var sellerId = this.jwt.getSubject();
        var result = this.productService.delete(id, sellerId);
        if (result.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!result.get()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("seller")
    @RolesAllowed("SELLER")
    public Response listBySeller() {
        var sellerId = this.jwt.getSubject();
        var products = this.productService.findBySeller(sellerId);
        return Response.ok(products).build();
    }
}
