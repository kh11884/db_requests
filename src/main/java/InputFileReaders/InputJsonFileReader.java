package InputFileReaders;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputJsonFileReader {
    private static org.json.simple.JSONObject inputJson = new org.json.simple.JSONObject();

    public static JSONArray readInputSearchFile(String fileName) throws IOException, ParseException {
        ReadJsonFile(fileName);
        JSONObject jsonObject = new JSONObject(inputJson);
        return jsonObject.getJSONArray("criterias");
    }

    public static JSONObject readInputStatFile(String fileName) throws IOException, ParseException {
        ReadJsonFile(fileName);
        return new JSONObject(inputJson);
    }

    private static void ReadJsonFile(String fileName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

         FileReader reader = new FileReader(fileName);
            inputJson = (org.json.simple.JSONObject) parser.parse(reader);
    }
}
