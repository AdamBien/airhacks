package airhacks.nova.amazon.control;

import org.json.JSONObject;

import airhacks.nova.logging.control.Log;

public interface Titan {

  /**
   * 
   * @param text       input
   * @param dimensions (1024, 512, and 256)
   * @return
   */
  static JSONObject payload(String text, int dimensions) {
    var payload = """
            {
              "inputText": ${inputText},
              "dimensions": ${dimensions},
              "normalize": true
            }
        """.replace("${inputText}", JSONObject.quote(text))
        .replace("${dimensions}", String.valueOf(dimensions));

    Log.debug(payload);
    return new JSONObject(payload);
  }

  static double[] extractEmbeddings(JSONObject result) {
    return result.getJSONArray("embedding")
        .toList()
        .stream()
        .map(obj -> ((Number) obj).doubleValue())
        .mapToDouble(Double::doubleValue)
        .toArray();
  }

}
