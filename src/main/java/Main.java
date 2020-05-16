import DBTools.DBConnector;
import JsonBuilders.LastNameSearch;
import JsonBuilders.ProductNameMinTimesSearch;
import JsonBuilders.SearchCriteria;
import JsonBuilders.SearchCriteriaJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        JSONObject lastNameCriteria = new JSONObject();
        lastNameCriteria.put("lastName", "Иванов");
        SearchCriteria lastNameSearch = new LastNameSearch(lastNameCriteria);

        JSONObject productNameMinTimesCriteria = new JSONObject();
        productNameMinTimesCriteria.put("productName", "Капуста")
                .put("minTimes", 4);
        SearchCriteria productNameMinTimesSearch = new ProductNameMinTimesSearch(productNameMinTimesCriteria);

        DBConnector connection = new DBConnector();
        SearchCriteriaJsonBuilder searchCriteria = new SearchCriteriaJsonBuilder();

        JSONObject testResultJson = null;
        JSONObject testResultJson2 = null;

        try {
            Connection con = connection.getDBConnecion();
            try {
                testResultJson = lastNameSearch.getJson(con);
                testResultJson2 = productNameMinTimesSearch.getJson(con);

            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testResultJson);
        System.out.println(testResultJson2);
    }
}
