import DBTools.DBConnector;
import JsonBuilders.SearchCriteriaJsonBuilder;
import org.json.JSONObject;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        JSONObject lastNameCriteria = new JSONObject();
        lastNameCriteria.put("lastName", "Иванов");

        DBConnector connection = new DBConnector();
        SearchCriteriaJsonBuilder searchCriteria = new SearchCriteriaJsonBuilder();

        JSONObject testResultJson = null;

        try {
            Connection con = connection.getDBConnecion();
            try {
                testResultJson = searchCriteria.buildLastNameCriteriaJSON(con, lastNameCriteria);

            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testResultJson);
    }
}
