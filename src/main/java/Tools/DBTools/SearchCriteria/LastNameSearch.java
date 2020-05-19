package Tools.DBTools.SearchCriteria;

import Tools.JsonTools.SearchJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LastNameSearch implements SearchCriteria {
    private final JSONObject lastNameCriteria;

    //TODO: добавить проверку правильности JSON в конструктор.
    public LastNameSearch(JSONObject lastNameCriteria) {
        this.lastNameCriteria = lastNameCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) {
        JSONObject resultJsonObject = new JSONObject();
        try {
            Statement stmt = connection.createStatement();
            String lastNameCriteriaString = lastNameCriteria.getString("lastName");
            ResultSet resultSet = stmt.executeQuery("SELECT lastname, firstname FROM customers\n" +
                    "WHERE lastname = '" + lastNameCriteriaString + "'");

            resultJsonObject.put("criteria", lastNameCriteria);
            resultJsonObject.put("results", SearchJsonBuilder.getResultsJsonArray(resultSet));

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}
