package JsonBuilders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LastNameSearch implements SearchCriteria {
    private final JSONObject lastNameCriteria;

    public LastNameSearch(JSONObject lastNameCriteria) {
        this.lastNameCriteria = lastNameCriteria;
    }

    public JSONObject getJson(Connection connection) {
        JSONObject resultJsonObject = new JSONObject();
        try {
            Statement stmt = connection.createStatement();
            String lastNameCriteriaString = lastNameCriteria.getString("lastName");
            ResultSet resultSet = stmt.executeQuery("SELECT lastname, firstname FROM customers\n" +
                    "WHERE lastname = '" + lastNameCriteriaString + "'");

            resultJsonObject.put("criteria", lastNameCriteria);
            resultJsonObject.put("results", new JSONArray());

            while (resultSet.next()) {
                resultJsonObject.append("results", new JSONObject()
                        .put("lastName", resultSet.getString(1))
                        .put("firstName", resultSet.getString(2)));
            }

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}
