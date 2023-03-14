package service;

import models.Nurse;
import models.Patient;
import models.Depot;
import models.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PopulationGenerator {
    public List<List<Integer>>listInitializer(int nurses){
        List<List<Integer>> listOfLists = new ArrayList<>();
        for(int i = 0; i<nurses; i++){
            listOfLists.add(new ArrayList<>());
        }
        return listOfLists;
    }

    public boolean checkValidity(Nurse nurse, Patient patient) {
        List<Patient> Patients = nurse.getListOfPatients();
        if (nurse.getListOfPatients().size() == 1) {
            System.out.println("Tom liste");
            return true;
        }
        if (nurse.getNurse_traveled() < patient.getEndWindow()-patient.getCareTime() && nurse.getNurse_traveled() > patient.getStartWindow()) {
            System.out.println("Time Window + care time");
            System.out.println(nurse.getCapacity()-patient.getDemand() + "   sajlhskldaf");
            if (nurse.getCapacity()-patient.getDemand() > 0) {
                System.out.println("demand");
                if(nurse.getNurse_traveled() + computeTravelDistance(nurse) + patient.getCareTime() < Settings.depot_return_time) {
                    System.out.println("Is Valid");
                    // TODO Pass på at pasientene til samme nurse ikke overlapper (caretime går over neste patient endWindow
                    return true;
                }
            }
        }
        return false;
    }

    public List<Nurse> generateRandom(List<Patient> patients, int amountOfNurses){
        //List<List<Integer>> listOfVisits = listInitializer(amountOfNurses);
        List<Patient> copyOfPatients = new ArrayList<>(patients);
        Depot depot = new Depot();
        List<Nurse> listOfNurses = depot.getAvailable_nurses();
        while(copyOfPatients.size() != 0){

            int randomPatientIndex = ThreadLocalRandom.current().nextInt(0, copyOfPatients.size());
            int randomNurseIndex = ThreadLocalRandom.current().nextInt(0, amountOfNurses);
            Patient patient = copyOfPatients.get(randomPatientIndex);
            Nurse nurse = listOfNurses.get(randomNurseIndex);
            int counter = 0;
            nurse.addListOfPatients(patient);
            boolean isValid = checkValidity(nurse, patient);

            while (!isValid && counter < 100) {
                //System.out.println("counter: " + counter);
                nurse.removeListOfPatients(patient);
                randomNurseIndex = ThreadLocalRandom.current().nextInt(0, amountOfNurses);
                nurse = listOfNurses.get(randomNurseIndex);
                nurse.addListOfPatients(patient);
                isValid = checkValidity(nurse, patient);
                counter++;
                if (counter == 100 && !isValid) {
                    nurse.removeListOfPatients(patient);
                }
            }
            if (isValid) {
                nurse.setNurse_traveled(computeTravelDistance(nurse));
                nurse.setCapacity(patient.getDemand());
                copyOfPatients.remove(patient);

                //Settings.setTotal_travel_time(patient.getCareTime() + computeTravelDistance(nurse));
            }
        }
        return listOfNurses;
    }

    public double computeTravelDistance(Nurse nurse) {
        List<Patient> listOfPatients = nurse.getListOfPatients();
        Patient patient = listOfPatients.get(0);
        int nurseId = nurse.getId();
        double traveled = 0.0;

        if (listOfPatients.size() == 1) {
            traveled = Settings.travelMatrix.get(nurseId).get(patient.getId());
            nurse.setNurse_traveled(traveled);
        } else {
            int fromPatient = listOfPatients.get(listOfPatients.size()-2).getId();
            int toPatient = listOfPatients.get(listOfPatients.size()-1).getId();
            traveled = Settings.travelMatrix.get(fromPatient).get(toPatient);
        }
        return traveled;
    }
}
