package Tools.JsonTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchCriteriaJsonBuilder {
    public JSONObject buildLastNameCriteriaJSON(Connection connection, JSONObject lastNameCriteria) {
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

    public JSONObject buildProductNameMinTimesCriteriaJSON(Connection connection, JSONObject productNameMinTimesCriteria) {
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
