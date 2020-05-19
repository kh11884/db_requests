package Tools.JsonTools;

import Tools.DBTools.DBStatQueries;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;

public class StatJsonBuilder {
    public static JSONObject getStatJson(Connection connection, String startDate, String endDate) {
        JSONObject resultJsonObject = new JSONObject();
        BigDecimal total = new BigDecimal(0);
        resultJsonObject.put("type", "stat")
                .put("TotalDays", DBStatQueries.getDaysQuantity(connection, startDate, endDate));

        ArrayList<JSONObject> customersList = DBStatQueries.getCustomersForPeriod(connection, startDate, endDate);

        for (JSONObject customer : customersList) {
            JSONObject customerPurchases = DBStatQueries.getCustomerExpensesForPeriod(connection, startDate, endDate, customer);
            resultJsonObject.append("customers", customerPurchases);
            total = total.add(customerPurchases.getBigDecimal("TotalExpenses"));
        }
        BigDecimal customersCount = new BigDecimal(customersList.size());

        resultJsonObject.put("TotalExpenses", total);
        resultJsonObject.put("AvgExpenses", total.divide(customersCount, 2, RoundingMode.HALF_UP));
        return resultJsonObject;
    }
}
