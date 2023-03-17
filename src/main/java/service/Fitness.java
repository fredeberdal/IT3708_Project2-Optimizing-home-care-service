package service;

import models.Nurse;
import models.Patient;
import models.Settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Fitness {
    public HashMap<List<Nurse>, Double> tup = new HashMap<List<Nurse>, Double>();

    public HashMap<List<Nurse>,Double> fitnessAndPopulation(List<Patient>patientList){
        PopulationGenerator pg = new PopulationGenerator();
        for(int i = 0; i< Settings.POP_SIZE; i++){
            List<Nurse> list = pg.generateRandom(patientList, Settings.number_of_nurses);
            tup.put(list, calculateFitness(list));
            list = null;
        }
        return tup;
    }
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
            totalTravelTime += nurse.getTime_traveled();
            if(nurse.getNurse_traveled() > Settings.depot_return_time){
                timeViolation += nurse.getNurse_traveled() - Settings.depot_return_time;
            }
        }
        fitness = totalTravelTime + (timeViolation * penalty);
        return fitness;
    }
}
