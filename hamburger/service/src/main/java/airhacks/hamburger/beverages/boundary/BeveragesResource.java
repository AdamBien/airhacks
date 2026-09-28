package airhacks.hamburger.beverages.boundary;

import static airhacks.hamburger.beverages.Requirement.Rn.R1_1;
import static airhacks.hamburger.beverages.Requirement.Rn.R1_2;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_1;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_2;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_3;
import static airhacks.hamburger.beverages.Requirement.Rn.R2_4;

import airhacks.hamburger.beverages.Requirement;
import airhacks.hamburger.beverages.control.Beverages;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("beverages")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BeveragesResource {

    @Inject
    Beverages beverages;

    @GET
    @Requirement({R1_1, R1_2})
    public Response listBeverages() {
        return Response.ok(this.beverages.all()).build();
    }

    @GET
    @Path("{name}")
    @Requirement({R2_1, R2_2, R2_3, R2_4})
    public Response findBeverage(@PathParam("name") String name) {
        if (name.isBlank()) {
            throw new BadRequestException("beverage name must not be blank");
        }
        var beverage = this.beverages.find(name)
                .orElseThrow(() -> new NotFoundException("no such beverage: " + name));
        return Response.ok(beverage).build();
    }
}
