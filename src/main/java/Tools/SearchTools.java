package Tools;

import Tools.FileTools.InputJsonFileReader;
import Tools.FileTools.OutputJsonFileWriter;
import Tools.DBTools.SearchCriteria.*;
import Tools.DBTools.DBConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class SearchTools {
    public void search(String inputFile, String outputFile) throws IOException, ParseException {
        JSONArray criterias = InputJsonFileReader.readInputSearchFile(inputFile);

        ArrayList<SearchCriteria> searchCriteriaArray = new ArrayList<SearchCriteria>();
        for (Object item : criterias) {
            JSONObject element = (JSONObject) item;
            switch (element.keySet().toString()) {
                case "[lastName]":
                    searchCriteriaArray.add(new LastNameSearch(element));
                    break;
                case "[minTimes, productName]":
                    searchCriteriaArray.add(new ProductNameMinTimesSearch(element));
                    break;
                case "[minExpenses, maxExpenses]":
                    searchCriteriaArray.add(new RangeExpensesSearch(element));
                    break;
                case "[badCustomers]":
                    searchCriteriaArray.add(new BadCustomersSearch(element));
                    break;
            }
        }

        JSONArray jsonArray = new JSONArray();
        DBConnector connection = new DBConnector();
        try (Connection con = connection.getDBConnecion()) {
            for (SearchCriteria searchCriteria : searchCriteriaArray) {
                jsonArray.put(searchCriteria.getJson(con));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject resultJson = new JSONObject()
                .put("type", "search")
                .put("results", jsonArray);

        OutputJsonFileWriter.writeJsonFile(outputFile, resultJson);
    }
}
