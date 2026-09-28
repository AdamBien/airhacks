package airhacks.hamburger.menu.boundary;

import static airhacks.hamburger.menu.Requirement.Rn.R1_1;
import static airhacks.hamburger.menu.Requirement.Rn.R1_2;
import static airhacks.hamburger.menu.Requirement.Rn.R2_1;
import static airhacks.hamburger.menu.Requirement.Rn.R2_2;

import airhacks.hamburger.menu.Requirement;
import airhacks.hamburger.menu.control.Menu;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MenuItemsResource {

    @Inject
    Menu menu;

    @GET
    @Requirement({R1_1, R1_2})
    public Response listMenu() {
        return Response.ok(this.menu.items()).build();
    }

    @GET
    @Path("{name}")
    @Requirement({R2_1, R2_2})
    public Response findItem(@PathParam("name") String name) {
        var item = this.menu.find(name)
                .orElseThrow(() -> new NotFoundException("no such menu item: " + name));
        return Response.ok(item).build();
    }
}
