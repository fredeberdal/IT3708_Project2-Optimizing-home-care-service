import models.Patient;
import models.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.VRPFileReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(new FileReader("src/main/java/resources/train_" + Settings.TASK_NUMBER + ".json"));

        VRPFileReader reader = new VRPFileReader();
        List<Patient> patientList = new ArrayList<Patient>();
        patientList = reader.fetchPatients();
        reader.fetchData(obj);
        reader.fetchTravelMatrix(obj);

        for(int p = 0; p < patientList.size(); p++) {
            System.out.println(patientList.get(p).getId());
        }
        System.out.println(Settings.travelMatrix);
    }
}
