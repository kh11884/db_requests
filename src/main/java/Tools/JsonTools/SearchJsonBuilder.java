package Tools.JsonTools;

import Tools.DBTools.SearchCriteria.SearchCriteria;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchJsonBuilder {
    public static JSONObject getSearchJson (Connection connection, ArrayList<SearchCriteria> searchCriteriaArray) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        for (SearchCriteria searchCriteria : searchCriteriaArray) {
            jsonArray.put(searchCriteria.getJson(connection));
        }

        return new JSONObject().put("type", "search")
                .put("results", jsonArray);
    }
}
