package airhacks.petstore.catalog.boundary;

import airhacks.petstore.catalog.control.CatalogStore;
import airhacks.petstore.catalog.entity.Item;
import airhacks.petstore.catalog.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("catalog")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CatalogResource {

    @Inject
    CatalogStore catalog;

    /** boundary op: list-categories (R1) */
    @GET
    @Path("categories")
    public Response listCategories() {
        var categories = Json.createArrayBuilder();
        this.catalog.categories().stream()
                .map(category -> category.toJSON())
                .forEach(categories::add);
        return Response.ok(categories.build()).build();
    }

    /** boundary op: list-products (R2) */
    @GET
    @Path("categories/{categoryId}/products")
    public Response listProducts(@PathParam("categoryId") String categoryId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        var products = this.catalog.productsOf(categoryId, page, size)
                .orElseThrow(() -> new NotFoundException("unknown category: " + categoryId));
        return Response.ok(products.toJSON(Product::toJSON)).build();
    }

    /** boundary op: list-items (R3) */
    @GET
    @Path("products/{productId}/items")
    public Response listItems(@PathParam("productId") String productId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        var items = this.catalog.itemsOf(productId, page, size)
                .orElseThrow(() -> new NotFoundException("unknown product: " + productId));
        return Response.ok(items.toJSON(Item::toJSON)).build();
    }

    /** boundary op: get-item (R4) */
    @GET
    @Path("items/{itemId}")
    public Response getItem(@PathParam("itemId") String itemId) {
        var item = this.catalog.item(itemId)
                .orElseThrow(() -> new NotFoundException("unknown item: " + itemId));
        return Response.ok(item.toJSON()).build();
    }

    /** boundary op: search-products (R5) */
    @GET
    @Path("search")
    public Response searchProducts(@QueryParam("keyword") String keyword,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        var matches = this.catalog.search(keyword == null ? "" : keyword, page, size);
        return Response.ok(matches.toJSON(Product::toJSON)).build();
    }
}
