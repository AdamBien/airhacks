package airhacks.nova.amazon.control;

import org.json.JSONObject;

import airhacks.nova.logging.control.Log;



public interface Nova {

    static JSONObject payload(String user, int maxTokens,float temperature, float topP) {
        var payload = """
                    {
                        "messages": [
                            {
                                "role": "user",
                                "content": [
                                    {
                                        "text": ${user}
                                    }
                                ]
                            }
                        ],
                        "inferenceConfig": {
                            "maxTokens": ${maxTokens},
                            "stopSequences": [],
                            "temperature": ${temperature},
                            "topP": ${topP}
                        }
                    }

                """.replace("${user}", JSONObject.quote(user))
                .replace("${maxTokens}", String.valueOf(maxTokens))
                .replace("${temperature}", String.valueOf(temperature))
                .replace("${topP}", String.valueOf(topP));

        Log.debug(payload);
        return new JSONObject(payload);
    }


    static String extractNovaText(JSONObject input) {
        return input.getJSONObject("output")
                .getJSONObject("message")
                .getJSONArray("content")
                .getJSONObject(0)
                .getString("text");
    }

}
