package Tools.JsonTools;

import Tools.DBTools.DBStatQueries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatJsonBuilder {
    public static JSONObject getStatJson(Connection connection, String startDate, String endDate) throws SQLException {
        JSONObject resultJsonObject = new JSONObject();
        BigDecimal total = new BigDecimal(0);
        resultJsonObject.put("type", "stat")
                .put("TotalDays", DBStatQueries.getDaysQuantity(connection, startDate, endDate))
                .put("customers", new JSONArray());

        ArrayList<JSONObject> customersList = DBStatQueries.getCustomersForPeriod(connection, startDate, endDate);

        for (JSONObject customer : customersList) {
            JSONObject customerPurchases = DBStatQueries.getCustomerExpensesForPeriod(connection, startDate, endDate, customer);
            resultJsonObject.append("customers", customerPurchases);
            total = total.add(customerPurchases.getBigDecimal("TotalExpenses"));
        }
        BigDecimal customersCount = new BigDecimal(customersList.size());

        if (customersCount.signum() == 0) {
            resultJsonObject.put("AvgExpenses", 0);
            resultJsonObject.put("TotalExpenses", 0);
        } else {
            resultJsonObject.put("AvgExpenses", total.divide(customersCount, 2, RoundingMode.HALF_UP));
            resultJsonObject.put("TotalExpenses", total);
        }
        return resultJsonObject;
    }
}
