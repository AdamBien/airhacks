package airhacks.qmp.menu.boundary;

import static airhacks.qmp.menu.Requirement.Rn.R1_1;
import static airhacks.qmp.menu.Requirement.Rn.R1_2;
import static airhacks.qmp.menu.Requirement.Rn.R1_3;
import static airhacks.qmp.menu.Requirement.Rn.R2_1;
import static airhacks.qmp.menu.Requirement.Rn.R2_2;

import airhacks.qmp.menu.Requirement;
import airhacks.qmp.menu.control.Catalog;
import airhacks.qmp.menu.entity.MenuItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MenuResource {

    @Inject
    Catalog catalog;

    /// `list-menu` — every sellable item with its price.
    @GET
    @Requirement({R1_1, R1_2, R1_3})
    public JsonArray listMenu() {
        var items = Json.createArrayBuilder();
        this.catalog.items().stream()
                .map(MenuItem::toJSON)
                .forEach(items::add);
        return items.build();
    }

    /// `get-item` — one sellable item by its name.
    @GET
    @Path("{name}")
    @Requirement({R2_1, R2_2})
    public JsonObject getItem(@PathParam("name") String name) {
        return this.catalog.find(name)
                .map(MenuItem::toJSON)
                .orElseThrow(() -> new NotFoundException("unknown menu item: " + name));
    }
}
