package Tools.JsonTools.SearchCriteriaTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomsJsonBuilder {
    public static JSONArray geCustomsJsonArray(ResultSet resultSet) throws SQLException {
        JSONArray resultJsonArray = new JSONArray();

        while (resultSet.next()) {
            resultJsonArray.put(new JSONObject()
                    .put("lastName", resultSet.getString(1))
                    .put("firstName", resultSet.getString(2)));
        }
        return resultJsonArray;
    }
}
