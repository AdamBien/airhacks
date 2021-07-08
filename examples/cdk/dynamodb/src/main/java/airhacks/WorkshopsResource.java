package airhacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.config.inject.ConfigProperty;


import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@Path("workshops")
public class WorkshopsResource {

    static final String DURATION = "duration";
    static final String ID = "id";
    static final String NAME = "name";

    @Inject
    DynamoDbClient dynamoDB;

    @Inject
    @ConfigProperty(name = "table.name", defaultValue = "workshops")
    String tableName;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonObject> workshops() {
        var request = ScanRequest.builder().tableName(this.tableName).attributesToGet(NAME, ID, DURATION).build();
        return dynamoDB.scanPaginator(request).items().stream().map(this::convert).toList();
    }
    
    JsonObject convert(Map<String, AttributeValue> item) {
        var id = item.get(ID).s();
        var name = item.get(NAME).s();
        var duration = item.get(DURATION).n();
        return Json.createObjectBuilder().add(DURATION, duration).add(NAME, name).add(ID, id).build();
    }

    // curl -XPOST -H'Content-type: application/json' -d'{"id":"2021.7","name":"AWS","duration":7}' http://localhost:8080/workshops
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String newWorkshop(JsonObject workshop) {
        var name       = workshop.getString(NAME);
        var id       = workshop.getString(ID);
        var duration = workshop.getJsonNumber(DURATION).intValue();
        var item = new HashMap<String, AttributeValue>();
        item.put(ID, AttributeValue.builder().s(id).build());
        item.put(NAME, AttributeValue.builder().s(name).build());
        item.put(DURATION, AttributeValue.builder().n(String.valueOf(duration)).build());

        var request = PutItemRequest.builder().tableName(this.tableName).item(item).build();    
        var response = this.dynamoDB.putItem(request);
        return response.responseMetadata().toString();
    }


}