/*
 */
package com.airhacks;

import java.util.Arrays;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.stream.JsonCollectors;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * http://www.adam-bien.com/roller/abien/entry/java_8_java_ee_7
 *
 * @author airhacks.com
 */
public final class JSONPTest {

    private List<JsonObject> list;

    @Before
    public void init() {
        this.list = Arrays.asList(next(), next());

    }


    public JsonObject next() {
        return Json.createObjectBuilder().
                add("number", System.currentTimeMillis()).
                build();
    }


    @Test
    public void toJsonArrayConversion() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        this.list.forEach(builder::add);
        JsonArray array = builder.build();
        assertThat(array.size(), is(list.size()));

    }

    @Test
    public void jsonArrayCollector() {
        JsonArray array = this.list.
                stream().
                collect(JsonCollectors.toJsonArray());
        assertThat(array.size(), is(list.size()));
    }

    @Test
    public void jsonObjectUpdate() {
        String key = "update";
        String JAVA_EE_8 = "is easy with Java EE 8";

        JsonObject initial = Json.createObjectBuilder().
                add(key, "was hard in Java EE 7").
                build();

        JsonObject updated = Json.createObjectBuilder(initial).
                add(key, JAVA_EE_8).
                build();
        assertThat(updated.getString(key), is(JAVA_EE_8));
        assertThat(updated.size(), is(1));
    }

    @Test
    public void patch() {

        JsonObject sub = Json.createObjectBuilder().
                add("static", "content").
                build();

        String key = "fact";
        JsonObject initial = Json.createObjectBuilder().
                add(key, "Java EE 8 is nice").
                add("another", "day").
                add("sub", sub).
                build();

        JsonPatch patch = Json.createPatchBuilder().
                add("/hey", "joe").
                replace("/" + key, "Java EE 8 rocks").
                replace("/sub/static", "updated content").
                remove("/another").
                build();

        JsonObject result = patch.apply(initial);
        System.out.println("result = " + result);

    }


}
