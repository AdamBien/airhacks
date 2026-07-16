package airhacks.contacts.contacts.boundary;

import jakarta.json.JsonObject;
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
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/contacts")
@RegisterRestClient(configKey = "base_uri")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContactsResourceClient {

    @GET
    Response listContacts();

    @GET
    @Path("{id}")
    Response getContact(@PathParam("id") String id);

    @POST
    Response createContact(JsonObject contact);

    @PUT
    @Path("{id}")
    Response updateContact(@PathParam("id") String id, JsonObject contact);

    @DELETE
    @Path("{id}")
    Response deleteContact(@PathParam("id") String id);
}
