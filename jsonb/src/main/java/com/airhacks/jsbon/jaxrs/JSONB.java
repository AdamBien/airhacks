
package com.airhacks.jsbon.jaxrs;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

/**
 *
 * @author airhacks.com
 */
public interface JSONB {

    public static Jsonb jsonb() {
        return JsonbBuilder.newBuilder().
                withConfig(new JsonbConfig().withPropertyVisibilityStrategy(new PrivateVisibilityStrategy())).
                build();
    }

}
