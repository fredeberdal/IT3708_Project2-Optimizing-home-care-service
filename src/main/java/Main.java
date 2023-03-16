import models.Nurse;
import models.Patient;
import models.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.PopulationGenerator;
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
        PopulationGenerator pg = new PopulationGenerator();


        List<Nurse> list = pg.generateRandom(patientList, Settings.number_of_nurses);
        double time = 0;
        for (Nurse nurse : list) {
            System.out.println("Find max_travel_time");
            if (time < nurse.getTime_traveled()) {
                time = nurse.getTime_traveled();
            }
        }
        Settings.setTotal_travel_time(time);


        int counter = 0;
        for (Nurse nurse : list) {
            System.out.println("New nurse");
            for (Patient p : nurse.getListOfPatients()) {
                System.out.println(p.getId());
                counter++;
            }
        }


        System.out.println("siste");
        System.out.println(counter);

    }
}
