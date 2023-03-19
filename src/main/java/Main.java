import models.Depot;
import models.Patient;
import models.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.Fitness;
import service.Individual;
import utils.VRPFileReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(new FileReader("src/main/java/resources/train_" + Settings.TASK_NUMBER + ".json"));

        VRPFileReader reader = new VRPFileReader();
        List<Patient> patientList = new ArrayList<Patient>();
        patientList = reader.fetchPatients();
        Settings.setPatients(patientList);
        reader.fetchData(obj);
        reader.fetchTravelMatrix(obj);

        Fitness fitness = new Fitness();
        Depot depot = new Depot();
        Individual ind = new Individual();
        //Individual pop = new Individual();


        //HashMap<List<Nurse>, Double> fit_pop= (patientList);
        //pop.generatePopulation(patientList);
        //Settings.individuals.forEach((k, v) -> System.out.println("Fitness : " + v));
        //Settings.individuals.forEach((k,v) -> k.get(0).getListOfPatients());
        //Optional<List<Nurse>> nurses = Settings.individuals.keySet().stream().findFirst();
        //List<Nurse> nurse = nurses.get();
        /*
        String s = "[";
        for(Nurse n : nurse){
            s += "[";
            for(Patient patients : n.getListOfPatients()){
                s += "" + patients.getId() +", ";
            }
            s += "], ";
        }
        s+= "]";
        System.out.println(s);
         */
    }
}
