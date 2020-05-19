package Tools.FileTools;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class InputJsonFileReader {
    public static JSONObject readInputStatFile(String fileName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(fileName);

        org.json.simple.JSONObject inputJson = (org.json.simple.JSONObject) parser.parse(reader);
        return new JSONObject(inputJson);
    }
}
