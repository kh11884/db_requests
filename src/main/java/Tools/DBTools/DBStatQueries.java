package Tools.DBTools;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBStatQueries {
    public static int getDaysQuantity(Connection connection, String startDate, String endDate) throws SQLException {
        int result = 0;

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT DATE('" + endDate + "') - DATE('" + startDate + "') + 1");
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        resultSet.close();
        stmt.close();

        if (result < 1) {
            throw new IllegalArgumentException("ƒата начала периода больше даты окончани€ периода.");
        }
        return result;
    }

    public static ArrayList<JSONObject> getCustomersForPeriod(Connection connection, String startDate, String endDate) {
        ArrayList<JSONObject> resultArray = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.lastname, c.firstname, SUM(p.price)\n" +
                    "FROM purchases pu\n" +
                    "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                    "         LEFT JOIN products p on pu.product_id = p.id\n" +
                    "WHERE pu.date BETWEEN '" + startDate + "' AND '" + endDate + "'\n" +
                    "GROUP BY c.lastname, c.firstname\n" +
                    "ORDER BY SUM(p.price) DESC;");
            while (resultSet.next()) {
                resultArray.add(new JSONObject()
                        .put("lastName", resultSet.getString(1))
                        .put("firstName", resultSet.getString(2)));
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    public static JSONObject getCustomerExpensesForPeriod(Connection connection, String startDate, String endDate, JSONObject customer) {
        JSONObject resultJsonObject = new JSONObject();
        String lastName = customer.getString("lastName");
        String firstName = customer.getString("firstName");
        resultJsonObject.put("name", lastName + " " + firstName);
        BigDecimal total = new BigDecimal(0.00);

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT p.productname, SUM(p.price)\n" +
                    "FROM purchases pu\n" +
                    "         LEFT JOIN customers c on pu.customer_id = c.id\n" +
                    "         LEFT JOIN products p on pu.product_id = p.id\n" +
                    "WHERE pu.date BETWEEN '" + startDate + "' AND '" + endDate + "'\n" +
                    "AND c.lastname LIKE '" + lastName + "' AND c.firstname LIKE '" + firstName + "'\n" +
                    "GROUP BY p.productname\n" +
                    "ORDER BY SUM(p.price) DESC;");

            while (resultSet.next()) {
                BigDecimal expenses = resultSet.getBigDecimal(2);
                resultJsonObject.append("purchases", new JSONObject()
                        .put("name", resultSet.getString(1))
                        .put("expenses", expenses));
                total = total.add(expenses);
            }
            resultJsonObject.put("TotalExpenses", total);

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }
}
