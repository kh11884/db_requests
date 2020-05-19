package Tools.DBTools.SearchCriteria;

import Tools.JsonTools.SearchCriteriaTools.CustomsJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LastNameSearch implements SearchCriteria {
    private final JSONObject lastNameCriteria;

    public LastNameSearch(JSONObject lastNameCriteria) {
        Object value = lastNameCriteria.get("lastName");
        if (value.getClass() != String.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии lastName.");
        }
        this.lastNameCriteria = lastNameCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) throws SQLException {
        JSONObject resultJsonObject = new JSONObject();
        Statement stmt = connection.createStatement();
        String lastNameCriteriaString = lastNameCriteria.getString("lastName");
        ResultSet resultSet = stmt.executeQuery("SELECT lastname, firstname FROM customers\n" +
                "WHERE lastname = '" + lastNameCriteriaString + "'");

        resultJsonObject.put("criteria", lastNameCriteria);
        resultJsonObject.put("results", CustomsJsonBuilder.geCustomsJsonArray(resultSet));

        resultSet.close();
        stmt.close();

        return resultJsonObject;
    }
}
