package FileWriters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class SearchJsonFileWriter {
    public static void writeSearchJsonFile (String fileName, JSONArray jsonArray){
        JSONObject resultJson = new JSONObject();
        resultJson.put("type", "search")
                .put("results", jsonArray);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(String.valueOf(resultJson));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
