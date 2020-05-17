package OutputFileWriters;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class OutputJsonFileWriter {
    public static void writeSearchJsonFile (String fileName, JSONObject outputJson){
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(String.valueOf(outputJson));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
