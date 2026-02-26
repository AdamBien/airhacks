package ro.tuica.distillery.catalog.boundary;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class TuicasResourceIT {

    @Inject
    @RestClient
    TuicasResourceClient client;

    @Test
    void allReturnsOk() {
        var response = client.all();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void getByNameReturnsOk() {
        var response = client.get("palinca");
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void storeCreates() {
        JsonObject tuica = Json.createObjectBuilder()
                .add("name", "palinca")
                .add("strength", 42)
                .build();
        var response = client.store(tuica);
        assertThat(response.getStatus()).isEqualTo(201);
    }
}
