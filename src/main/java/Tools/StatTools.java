package Tools;

import InputFileReaders.InputJsonFileReader;
import OutputFileWriters.OutputJsonFileWriter;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StatTools {
    public void getStatistic(String inputFile, String outputFile) throws IOException, ParseException {
        JSONObject inputJson = InputJsonFileReader.readInputStatFile(inputFile);

        String startDate = inputJson.getString("startDate");
        String endDate = inputJson.getString("endDate");

        DBConnector connection = new DBConnector();
        try (Connection con = connection.getDBConnecion()) {
            OutputJsonFileWriter.writeJsonFile(outputFile, getExpensesForPeriod(con, startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getExpensesForPeriod(Connection connection, String startDate, String endDate) {
        JSONObject resultJsonObject = new JSONObject();
        BigDecimal total = new BigDecimal(0);
        resultJsonObject.put("type", "stat")
                .put("TotalDays", getDays(connection, startDate, endDate));

        ArrayList<JSONObject> customersList = getCustomersForPeriod(connection, startDate, endDate);

        for (JSONObject customer : customersList) {
            JSONObject customerPurchases = getCustomerExpensesForPeriod(connection, startDate, endDate, customer);
            resultJsonObject.append("customers", customerPurchases);
            total = total.add(customerPurchases.getBigDecimal("TotalExpenses"));
        }
        BigDecimal customersCount = new BigDecimal(customersList.size());

        resultJsonObject.put("TotalExpenses", total);
        resultJsonObject.put("AvgExpenses", total.divide(customersCount, 2, RoundingMode.HALF_UP));
        return resultJsonObject;
    }

    private int getDays(Connection connection, String startDate, String endDate) {
        int result = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT DATE('" + endDate + "') - DATE('" + startDate + "') + 1");
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result < 1){
            throw  new IllegalArgumentException("ƒата начала периода больше даты окончани€ периода");
        }
        return result;
    }

    private ArrayList<JSONObject> getCustomersForPeriod(Connection connection, String startDate, String endDate) {
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

    private JSONObject getCustomerExpensesForPeriod(Connection connection, String startDate, String endDate, JSONObject customer) {
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
