import DBTools.DBConnector;
import InputFileReaders.InputSearchFileReader;
import OutputFileWriters.SearchJsonFileWriter;
import SearchCriteria.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;

public class SearchTools {
    public static void search (String inputFile, String outputFile){
        JSONArray criterias = InputSearchFileReader.readInputSearchFile(inputFile);
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
        SearchJsonFileWriter.writeSearchJsonFile(outputFile, jsonArray);
    }
}
