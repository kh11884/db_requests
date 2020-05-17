package SearchCriteria;

import JsonBuilders.SearchJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RangeExpensesSearch implements SearchCriteria {
    private final JSONObject rangeExpensesCriteria;

    //TODO: добавить проверку правильности JSON в конструктор.
    public RangeExpensesSearch(JSONObject rangeExpensesCriteria) {
        this.rangeExpensesCriteria = rangeExpensesCriteria;
    }

    public JSONObject getJson(Connection connection) {
        JSONObject resultJsonObject = new JSONObject();
        try {
            Statement stmt = connection.createStatement();
            int minExpenses = rangeExpensesCriteria.getInt("minExpenses");
            int maxExpenses = rangeExpensesCriteria.getInt("maxExpenses");

            ResultSet resultSet = stmt.executeQuery("SELECT c.lastname, c.firstname, SUM(p.price)\n" +
                    "FROM purchases pu\n" +
                    "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                    "         LEFT JOIN products p on pu.product_id = p.id\n" +
                    "GROUP BY c.lastname, c.firstname\n" +
                    "HAVING SUM(p.price) BETWEEN " + minExpenses + " AND " + maxExpenses + ";");

            resultJsonObject.put("criteria", rangeExpensesCriteria);
            resultJsonObject.put("results", SearchJsonBuilder.getResultsJsonArray(resultSet));

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}