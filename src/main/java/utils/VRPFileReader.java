package utils;

import models.Patient;
import models.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VRPFileReader { // Lese fra interface #n filer

    JSONParser parser = new JSONParser();
    List<Patient> patientList = new ArrayList<>();

    public List<Patient> fetchPatients() throws IOException, ParseException {
        
        JSONObject obj = (JSONObject) parser.parse(new FileReader("src/main/java/resources/train_" + Settings.TASK_NUMBER + ".json"));
        JSONObject patientsJson = (JSONObject) obj.get("patients");
        //patientList.add(null);
        for (int i = 1; i < 101; i++) {
            JSONObject JSONpatient = (JSONObject) patientsJson.get("" + i);
            Long x_coord = (Long) JSONpatient.get("x_coord");
            Long y_coord = (Long) JSONpatient.get("y_coord");
            Long start_time = (Long) JSONpatient.get("start_time");
            Long end_time = (Long) JSONpatient.get("end_time");
            Long care_time = (Long) JSONpatient.get("care_time");
            Long demand = (Long) JSONpatient.get("demand");

            Patient patient = new Patient(i, start_time.intValue(), end_time.intValue(),
                    care_time.intValue(), demand.intValue(), x_coord.intValue(), y_coord.intValue());
            patientList.add(patient);
        }
        return patientList;
    }

}