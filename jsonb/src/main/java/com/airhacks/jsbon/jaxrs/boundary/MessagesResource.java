
package com.airhacks.jsbon.jaxrs.boundary;

import static com.airhacks.jsbon.jaxrs.JSONB.jsonb;
import com.airhacks.jsbon.jaxrs.entity.Message;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Adam Bien
 */
@Path("messages")
public class MessagesResource {

    @GET
    public List<Message> message() {
        return Arrays.asList(new Message("hello"), new Message("world"));
    }

    //http://localhost:8080/jsonb/resources/messages/duke
    @GET
    @Path("{msg}")
    public String message(@PathParam("msg") String message) {
        String retVal = jsonb().toJson(new Message(message));
        System.out.println("retVal = " + retVal);
        return retVal;
    }



}
