package Tools;

import Tools.FileTools.InputJsonFileReader;
import Tools.JsonTools.StatJsonBuilder;
import Tools.FileTools.OutputJsonFileWriter;
import Tools.DBTools.DBConnector;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class StatTools {
    public void getStatistic(String inputFile, String outputFile) throws IOException, ParseException, SQLException {
        JSONObject inputJson = InputJsonFileReader.readInputStatFile(inputFile);

        String startDateKey = "startDate";
        String endDateKey = "endDate";

        checkDate(startDateKey, inputJson);
        checkDate(endDateKey, inputJson);

        String startDate = inputJson.getString(startDateKey);
        String endDate = inputJson.getString(endDateKey);

        DBConnector connection = new DBConnector();
        Connection con = connection.getDBConnecion();
        OutputJsonFileWriter.writeJsonFile(outputFile, StatJsonBuilder.getStatJson(con, startDate, endDate));
        con.close();
    }

    private void checkDate(String key, JSONObject inputJson) {
        if (!inputJson.has(key)) {
            throw new IllegalArgumentException("В входном файле отсутсвует критерий " + key);
        }
        Object value = inputJson.get(key);
        if (value.getClass() != String.class) {
            throw new IllegalArgumentException("Неверно указано значение в критерии " + key);
        }
    }
}
