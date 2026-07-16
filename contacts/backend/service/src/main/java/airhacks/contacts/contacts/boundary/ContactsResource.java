package airhacks.contacts.contacts.boundary;

import airhacks.contacts.contacts.control.ContactsStore;
import airhacks.contacts.contacts.entity.Contact;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactsResource {

    @Inject
    ContactsStore store;

    @GET
    public Response listContacts() {
        var contacts = Json.createArrayBuilder();
        this.store.all().stream()
                .map(Contact::toJSON)
                .forEach(contacts::add);
        return Response.ok(contacts.build()).build();
    }

    @GET
    @Path("{id}")
    public Response getContact(@PathParam("id") String id) {
        return this.store.find(id)
                .map(contact -> Response.ok(contact.toJSON()).build())
                .orElseThrow(NotFoundException::new);
    }

    @POST
    public Response createContact(JsonObject json, @Context UriInfo uriInfo) {
        var contact = Contact.fromJSON(json);
        if (!contact.isValid()) {
            throw new BadRequestException("a contact requires a last name, a business or private type, and a well-formed email");
        }
        var stored = this.store.create(contact);
        var location = uriInfo.getAbsolutePathBuilder().path(stored.id()).build();
        return Response.created(location).entity(stored.toJSON()).build();
    }

    @PUT
    @Path("{id}")
    public Response updateContact(@PathParam("id") String id, JsonObject json) {
        var contact = Contact.fromJSON(json);
        if (!contact.isValid()) {
            throw new BadRequestException("a contact requires a last name, a business or private type, and a well-formed email");
        }
        return this.store.update(id, contact)
                .map(updated -> Response.ok(updated.toJSON()).build())
                .orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("{id}")
    public Response deleteContact(@PathParam("id") String id) {
        if (!this.store.delete(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }
}
