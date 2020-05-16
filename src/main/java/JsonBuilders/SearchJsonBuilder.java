package JsonBuilders;

import com.sun.org.apache.bcel.internal.generic.JsrInstruction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchJsonBuilder {
    public static JSONArray getResultsJsonArray (ResultSet resultSet) throws SQLException {
        JSONArray resultJsonArray = new JSONArray();
        while (resultSet.next()) {
            resultJsonArray.put(new JSONObject()
                    .put("lastName", resultSet.getString(1))
                    .put("firstName", resultSet.getString(2)));
        }
        return resultJsonArray;
    }
}
