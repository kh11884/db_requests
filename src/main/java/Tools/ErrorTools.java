package Tools;

import JsonBuilders.ErrorJsonBuilder;
import OutputFileWriters.OutputJsonFileWriter;
import org.json.JSONObject;

public class ErrorTools {
    public void setError (String outputFile, Exception exception){
        JSONObject errorJson = ErrorJsonBuilder.getErrorJson(exception);
        OutputJsonFileWriter.writeJsonFile(outputFile, errorJson);
    }
}
