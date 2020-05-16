import DBTools.DBConnector;
import JsonBuilders.SearchCriteriaJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        JSONObject lastNameCriteria = new JSONObject();
        lastNameCriteria.put("lastName", "Иванов");

        JSONObject productNameMinTimesCriteria = new JSONObject();
        productNameMinTimesCriteria.put("productName", "Капуста")
                .put("minTimes", 4);

        DBConnector connection = new DBConnector();
        SearchCriteriaJsonBuilder searchCriteria = new SearchCriteriaJsonBuilder();

        JSONObject testResultJson = null;
        JSONObject testResultJson2 = null;

        try {
            Connection con = connection.getDBConnecion();
            try {
                testResultJson = searchCriteria.buildLastNameCriteriaJSON(con, lastNameCriteria);
                testResultJson2 = searchCriteria.buildProductNameMinTimesCriteriaJSON(con, productNameMinTimesCriteria);

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
