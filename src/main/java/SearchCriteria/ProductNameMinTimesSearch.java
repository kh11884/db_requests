package SearchCriteria;

import JsonBuilders.SearchJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductNameMinTimesSearch implements SearchCriteria {
    private final JSONObject productNameMinTimesCriteria;

    //TODO: добавить проверку правильности JSON в конструктор.
    public ProductNameMinTimesSearch(JSONObject productNameMinTimesCriteria) {
        this.productNameMinTimesCriteria = productNameMinTimesCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) {
        JSONObject resultJsonObject = new JSONObject();
        try {
            String productNameCriteria = productNameMinTimesCriteria.getString("productName");
            int minTimesCriteria = productNameMinTimesCriteria.getInt("minTimes");

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.lastname, c.firstname, p.productname, COUNT(*)\n" +
                    "FROM purchases pu\n" +
                    "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                    "         LEFT JOIN products p on pu.product_id = p.id\n" +
                    "WHERE p.productname = '" + productNameCriteria + "'\n" +
                    "GROUP BY c.lastname, c.firstname, p.productname\n" +
                    "HAVING COUNT(*) >=+" + minTimesCriteria + ";");

            resultJsonObject.put("criteria", productNameMinTimesCriteria);
            resultJsonObject.put("results", SearchJsonBuilder.getResultsJsonArray(resultSet));

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}
