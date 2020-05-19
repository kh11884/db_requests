package Tools;

import Tools.FileTools.InputJsonFileReader;
import Tools.FileTools.OutputJsonFileWriter;
import Tools.DBTools.SearchCriteria.*;
import Tools.DBTools.DBConnector;
import Tools.JsonTools.SearchJsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchTools {
    public void search(String inputFile, String outputFile) throws IOException, ParseException, SQLException {
        JSONObject inputJson = InputJsonFileReader.readInputStatFile(inputFile);

        if (!inputJson.has("criterias")) {
            throw new IllegalArgumentException("В входном файле отсутсвуют критерии для поиска.");
        }
        JSONArray criterias = inputJson.getJSONArray("criterias");

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
                default:
                    throw new IllegalArgumentException("Передан неизвестный критерий для поиска");
            }
        }
        DBConnector connection = new DBConnector();
        Connection con = connection.getDBConnecion();
        JSONObject resultJson = SearchJsonBuilder.getSearchJson(con, searchCriteriaArray);
        con.close();

        OutputJsonFileWriter.writeJsonFile(outputFile, resultJson);
    }
}
