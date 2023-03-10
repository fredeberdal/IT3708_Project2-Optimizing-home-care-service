import models.Patient;
import org.json.simple.parser.ParseException;
import utils.VRPFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Christian!");

        VRPFileReader reader = new VRPFileReader();
        List<Patient> patientList = new ArrayList<Patient>();
        patientList = reader.fetchPatients();
        for(int p = 0; p < patientList.size(); p++) {
            System.out.println(patientList.get(p).getCareTime());
        }
        System.out.println("Ja");
    }
}
