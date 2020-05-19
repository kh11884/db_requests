package Tools.DBTools.SearchCriteria;

import Tools.JsonTools.SearchCriteriaTools.CustomsJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RangeExpensesSearch implements SearchCriteria {
    private final JSONObject rangeExpensesCriteria;

    public RangeExpensesSearch(JSONObject rangeExpensesCriteria) {
        Object valueMin = rangeExpensesCriteria.get("minExpenses");
        Object valueMax = rangeExpensesCriteria.get("maxExpenses");

        if (valueMin.getClass() != Long.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии minExpenses.");
        }
        if (rangeExpensesCriteria.getInt("minExpenses") < 0) {
            throw new IllegalArgumentException("«начение в критерии minExpenses не может быть меньше 0.");
        }
        if (valueMax.getClass() != Long.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии maxExpenses.");
        }
        if (rangeExpensesCriteria.getInt("maxExpenses") < 0) {
            throw new IllegalArgumentException("«начение в критерии maxExpenses не может быть меньше 0.");
        }
        if (rangeExpensesCriteria.getInt("maxExpenses") < rangeExpensesCriteria.getInt("minExpenses")) {
            throw new IllegalArgumentException("«начение в критерии maxExpenses не может быть меньше значени€ в критерии  minExpenses.");
        }
        this.rangeExpensesCriteria = rangeExpensesCriteria;
    }

    public JSONObject getJson(Connection connection) throws SQLException {
        JSONObject resultJsonObject = new JSONObject();

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
        resultJsonObject.put("results", CustomsJsonBuilder.geCustomsJsonArray(resultSet));

        resultSet.close();
        stmt.close();

        return resultJsonObject;
    }
}
