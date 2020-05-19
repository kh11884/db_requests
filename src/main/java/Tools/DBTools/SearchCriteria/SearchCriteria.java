package Tools.DBTools.SearchCriteria;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;

public interface SearchCriteria {


    JSONObject getJson(Connection connection) throws SQLException;
}
