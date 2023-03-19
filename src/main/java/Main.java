import models.Depot;
import models.Nurse;
import models.Patient;
import models.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.Fitness;
import service.Individual;
import service.Population;
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
        Depot depot = new Depot();
        Population pop = new Population();
        pop = pop.makePopulation();
        pop.sortPop();
        pop.print();
        double best = 10000;

        /*
        for(int i = 0; i<10000; i++){
            pop = pop.generateNewGen(pop);
            if(pop.individuals.get(0).fitness < best){
                best = pop.individuals.get(0).fitness;
                System.out.println("Bedre: " + best);
            }else{
                System.out.println("Not better: " + best);
            }
        }

         */
        //pop.print();
        //Population pop = new Population();
        //pop.sortPop();
        //Population newPop = pop.generateNewGen(pop);
        //Individual pop = new Individual();


        //HashMap<List<Nurse>, Double> fit_pop= (patientList);
        //pop.generatePopulation(patientList);
        //Settings.individuals.forEach((k,v) -> k.get(0).getListOfPatients());
        //Optional<List<Nurse>> nurses = Settings.individuals.keySet().stream().findFirst();
        //List<Nurse> nurse = nurses.get();

        /*
        String s = "[";
        for(Individual n : pop.individuals){
            for(Nurse nurse : n.nurses){
                s += "[";
                for(Patient p : nurse.getListOfPatients()) {
                    s += "" + p.getId() + ", ";
                }
                s += "], ";

            }
        }
        s+= "]";
        System.out.println(s);

         */


    }
}
