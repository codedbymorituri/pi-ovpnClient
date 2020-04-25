package helpers;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class Json {

    private List jsonClassObject;
    private String errorMessage;

    public List getJsonClassObject() {
        return jsonClassObject;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean readFromJsonFile(String inputFile, Class jsonClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        try {
            CollectionType classList = mapper.getTypeFactory().constructCollectionType(List.class, jsonClass);
            jsonClassObject = mapper.readValue(new FileInputStream(inputFile), classList);
            return true;
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            return false;
        }
    }

    public boolean writeJsonToFile(Object jsonClass, String outputFile) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(outputFile), jsonClass);
            return true;
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            return false;
        }
    }

}
