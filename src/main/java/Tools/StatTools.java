package Tools;

import Tools.FileTools.InputJsonFileReader;
import Tools.JsonTools.StatJsonBuilder;
import Tools.FileTools.OutputJsonFileWriter;
import Tools.DBTools.DBConnector;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;


public class StatTools {
    public void getStatistic(String inputFile, String outputFile) throws IOException, ParseException {
        JSONObject inputJson = InputJsonFileReader.readInputStatFile(inputFile);

        String startDate = inputJson.getString("startDate");
        String endDate = inputJson.getString("endDate");

        DBConnector connection = new DBConnector();
        try (Connection con = connection.getDBConnecion()) {
            OutputJsonFileWriter.writeJsonFile(outputFile, StatJsonBuilder.getStatJson(con, startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
