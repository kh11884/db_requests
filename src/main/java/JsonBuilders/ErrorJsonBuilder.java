package JsonBuilders;

import org.json.JSONObject;

public class ErrorJsonBuilder {
    public static JSONObject getErrorJson (Exception exception){
        return new JSONObject()
                .put("type", "error")
                .put("message", exception.getMessage());
    }
}
