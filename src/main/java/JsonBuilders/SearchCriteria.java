package JsonBuilders;

import org.json.JSONObject;

import java.sql.Connection;

public interface SearchCriteria {
    public JSONObject getJson(Connection connection);
}
