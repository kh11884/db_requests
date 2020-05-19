package Tools.DBTools.SearchCriteria;

import Tools.JsonTools.SearchCriteriaTools.CustomsJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductNameMinTimesSearch implements SearchCriteria {
    private final JSONObject productNameMinTimesCriteria;

    public ProductNameMinTimesSearch(JSONObject productNameMinTimesCriteria) {
        Object productName = productNameMinTimesCriteria.get("productName");
        if (productName.getClass() != String.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии productName.");
        }

        Object minTimes = productNameMinTimesCriteria.get("minTimes");
        if (minTimes.getClass() != Long.class) {
            throw new IllegalArgumentException("Ќеверно указано значение в критерии minTimes.");
        }
        if (productNameMinTimesCriteria.getInt("minTimes") < 1) {
            throw new IllegalArgumentException("«начение в критерии minTimes не может быть меньше 1.");
        }

        this.productNameMinTimesCriteria = productNameMinTimesCriteria;
    }

    @Override
    public JSONObject getJson(Connection connection) throws SQLException {
        JSONObject resultJsonObject = new JSONObject();

        String productNameCriteria = productNameMinTimesCriteria.getString("productName");
        int minTimesCriteria = productNameMinTimesCriteria.getInt("minTimes");

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT c.lastname, c.firstname\n" +
                "FROM purchases pu\n" +
                "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                "         LEFT JOIN products p on pu.product_id = p.id\n" +
                "WHERE LOWER(p.productname) = '" + productNameCriteria.toLowerCase() + "'\n" +
                "GROUP BY c.lastname, c.firstname\n" +
                "HAVING COUNT(*) >=+" + minTimesCriteria + ";");

        resultJsonObject.put("criteria", productNameMinTimesCriteria)
                .put("results", CustomsJsonBuilder.geCustomsJsonArray(resultSet));

        resultSet.close();
        stmt.close();

        return resultJsonObject;
    }
}
