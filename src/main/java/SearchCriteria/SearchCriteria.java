package SearchCriteria;

import org.json.JSONObject;

import java.sql.Connection;

public interface SearchCriteria {


    JSONObject getJson(Connection connection);
}
