import DBTools.DBConnector;
import FileWriters.SearchJsonFileWriter;
import SearchCriteria.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        ArrayList<SearchCriteria> searchCriteriaArray = new ArrayList<SearchCriteria>();
        JSONObject lastNameCriteria = new JSONObject();
        lastNameCriteria.put("lastName", "Иванов");
        searchCriteriaArray.add(new LastNameSearch(lastNameCriteria));

        JSONObject productNameMinTimesCriteria = new JSONObject();
        productNameMinTimesCriteria.put("productName", "Капуста")
                .put("minTimes", 4);
        searchCriteriaArray.add(new ProductNameMinTimesSearch(productNameMinTimesCriteria));

        JSONObject rangeExpensesCriteria = new JSONObject();
        rangeExpensesCriteria.put("minExpenses", 1100)
                .put("maxExpenses", 1200);
        searchCriteriaArray.add(new RangeExpensesSearch(rangeExpensesCriteria));

        JSONObject badCustomersCriteria = new JSONObject();
        badCustomersCriteria.put("badCustomers", 3);
        searchCriteriaArray.add(new BadCustomersSearch(badCustomersCriteria));

        JSONArray jsonArray = new JSONArray();

        DBConnector connection = new DBConnector();

        try (Connection con = connection.getDBConnecion()) {
            for (SearchCriteria searchCriteria : searchCriteriaArray) {
                    jsonArray.put(searchCriteria.getJson(con));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchJsonFileWriter.writeSearchJsonFile("output.json", jsonArray);
    }
}
