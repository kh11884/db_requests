package Tools.DBTools.SearchCriteria;

import JsonBuilders.SearchJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BadCustomersSearch implements SearchCriteria {
    private final JSONObject badCustomersCriteria;

    //TODO: добавить проверку правильности JSON в конструктор.
    public BadCustomersSearch(JSONObject badCustomersCriteria) {
        this.badCustomersCriteria = badCustomersCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) {
        JSONObject resultJsonObject = new JSONObject();
        try {
            Statement stmt = connection.createStatement();
            int badCustomersCount = badCustomersCriteria.getInt("badCustomers");
            ResultSet resultSet = stmt.executeQuery("SELECT c.lastname, c.firstname, SUM(p.price)\n" +
                    "FROM purchases pu\n" +
                    "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                    "         LEFT JOIN products p on pu.product_id = p.id\n" +
                    "GROUP BY c.lastname, c.firstname\n" +
                    "ORDER BY SUM(p.price)\n" +
                    "LIMIT " + badCustomersCount);

            resultJsonObject.put("criteria", badCustomersCriteria);
            resultJsonObject.put("results", SearchJsonBuilder.getResultsJsonArray(resultSet));

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}
