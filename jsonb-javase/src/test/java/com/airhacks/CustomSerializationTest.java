/*
 */
package com.airhacks;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CustomSerializationTest {

    @Test
    public void serializeIntoString() {
        String serialized = JsonbBuilder.
                newBuilder().
                withConfig(new JsonbConfig().
                        withPropertyVisibilityStrategy(new PrivateVisibilityStrategy())).
                build().
                toJson(new Message("duke"));
        System.out.println("retVal = " + serialized);
        assertThat(serialized, containsString("duke"));
    }
    @Test
    public void serializeIntoStringWithoutCustomStrategy() {
        String serialized = JsonbBuilder.create().
                toJson(new Message("duke"));
        System.out.println("retVal = " + serialized);
        assertThat(serialized, containsString("duke"));
    }


}
