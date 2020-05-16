package InputFileReaders;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class InputSearchFileReader {
    public static JSONArray readInputSearchFile (String fileName){
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject inputJson = new org.json.simple.JSONObject();
        try (FileReader reader = new FileReader(fileName)) {
            inputJson = (org.json.simple.JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(inputJson);
        return jsonObject.getJSONArray("criterias");
    }
}
