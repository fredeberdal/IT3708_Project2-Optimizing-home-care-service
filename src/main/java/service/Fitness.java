package service;

import models.Nurse;
import models.Patient;
import models.Settings;

import java.util.HashMap;
import java.util.List;

public class Fitness {
    public HashMap<List<Nurse>, Double> tup = new HashMap<List<Nurse>, Double>();

    /*
    public HashMap<List<Nurse>,Double> fitnessAndPopulation(List<Patient>patientList){
        Individual pg = new Individual();
        for(int i = 0; i< Settings.POP_SIZE; i++){
            List<Nurse> list = pg.generateRandom(patientList, Settings.number_of_nurses);
            tup.put(list, calculateFitness(list));
            list = null;
        }
        return tup;
    }

     */
    public double max_fitness(List<Nurse> nurses){
        double max = 0;
        for(Nurse nurse : nurses){
            if (max < nurse.getNurse_traveled()) {
                max = nurse.getNurse_traveled();
            }
        }
        return max;
    }
    public double calculateFitness(List<Nurse> nurses){
        double fitness = 0;
        int penalty = 1000;
        double timeViolation = 0.0;
        double totalTravelTime = 0.0;
        for(Nurse nurse : nurses){
            System.out.println(nurse.getListOfPatients().size());
            totalTravelTime += calculateRoute(nurse);
            if(nurse.getNurse_traveled() > Settings.depot_return_time){
                timeViolation += nurse.getNurse_traveled() - Settings.depot_return_time;
            }
        }
        fitness = totalTravelTime + (timeViolation * penalty);
        return fitness;
    }

    public double calculateRoute(Nurse nurse){
        double totalTravelTime = 0;
        int p=0;
        for(int i = 0; i<nurse.getListOfPatients().size(); i++){
            if(i == nurse.getListOfPatients().size()){
                totalTravelTime += Settings.travelMatrix.get(0).get(nurse.getListOfPatients().get(i).getId());
                System.out.println(totalTravelTime);
            }
        }
        return totalTravelTime;
    }
}
