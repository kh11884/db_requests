package Tools.JsonTools;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class ErrorJsonBuilder {
    public static JSONObject getErrorJson(Exception exception) {
        JSONObject resultJson = new JSONObject()
                .put("type", "error");

        if (exception.getClass() == ParseException.class) {
            resultJson.put("message", "Ошибка парсинга входного файла. Дополнительно: " + exception.toString());
        } else {
            resultJson.put("message", exception.getMessage());
        }
        return resultJson;
    }
}
