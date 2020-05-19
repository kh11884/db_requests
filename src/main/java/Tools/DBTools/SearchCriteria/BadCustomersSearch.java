package Tools.DBTools.SearchCriteria;

import Tools.JsonTools.SearchCriteriaTools.CustomsJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BadCustomersSearch implements SearchCriteria {
    private final JSONObject badCustomersCriteria;

    public BadCustomersSearch(JSONObject badCustomersCriteria) {
        Object value = badCustomersCriteria.get("badCustomers");

        if (value.getClass() != Long.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии badCustomers.");
        }
        if (badCustomersCriteria.getInt("badCustomers") < 1) {
            throw new IllegalArgumentException("«начение в критерии badCustomers не может быть меньше 1.");
        }
        this.badCustomersCriteria = badCustomersCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) throws SQLException {
        JSONObject resultJsonObject = new JSONObject();

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
        resultJsonObject.put("results", CustomsJsonBuilder.geCustomsJsonArray(resultSet));

        resultSet.close();
        stmt.close();

        return resultJsonObject;
    }
}
