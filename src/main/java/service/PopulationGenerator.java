package service;

import models.Nurse;
import models.Patient;
import models.Depot;
import models.Settings;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PopulationGenerator {
    //public HashMap<List<Nurse>, Double> tup = new HashMap<List<Nurse>, Double>();


    public void generatePopulation(List<Patient> patientList) {
        PopulationGenerator pg = new PopulationGenerator();
        HashMap<List<Nurse>, Double> tup = new HashMap<List<Nurse>, Double>();
        Fitness fitness = new Fitness();
        for (int i = 0; i < Settings.POP_SIZE; i++) {
            List<Nurse> list = pg.generateRandom(patientList, Settings.number_of_nurses);
            tup.put(list, fitness.calculateFitness(list));
            list = null;
        }
        sortIndividuals(tup);

    }

    public void sortIndividuals(HashMap<List<Nurse>, Double> individuals) {
        ArrayList<Double> list = new ArrayList<>();
        for (Map.Entry<List<Nurse>, Double> i : individuals.entrySet()) {
            list.add(i.getValue());
        }
        Collections.sort(list);
        LinkedHashMap<List<Nurse>, Double> sortedMap = new LinkedHashMap<>();
        for (double fit : list) {
            for (Map.Entry<List<Nurse>, Double> entry : individuals.entrySet()) {
                if (entry.getValue().equals(fit)) {
                    Settings.individuals.put(entry.getKey(), fit);
                }
            }
        }
    }


    public boolean checkValidity(Nurse nurse, Patient patient, int counter) {

        if (nurse.getListOfPatients().size() == 1) {
            return true;
        }

        if (counter > 50 && nurse.getCapacity() > patient.getDemand() && nurse.getNurse_traveled() < patient.getStartWindow()) {
            nurse.setNurse_traveled(patient.getStartWindow() - nurse.getNurse_traveled());
        }
        if (nurse.getNurse_traveled() <= patient.getEndWindow() - patient.getCareTime() && nurse.getNurse_traveled() >= patient.getStartWindow()) {
            if (nurse.getCapacity() - patient.getDemand() > 0) {
                if (nurse.getNurse_traveled() + computeTravelDistance(nurse, false) + patient.getCareTime() < Settings.depot_return_time) {
                    return true;
                    // TODO Pass på at pasientene til samme nurse ikke overlapper (caretime går over neste patient endWindow
                }
            }
        }
        return false;
    }

    public List<Nurse> generateRandom(List<Patient> patients, int amountOfNurses) {
        List<Patient> copyOfPatients = new ArrayList<>(patients);
        Depot depot = new Depot();
        int d = 0;
        List<Nurse> listOfNurses = depot.getAvailable_nurses();
        while (!copyOfPatients.isEmpty()) {
            int counter = 0;

            int randomNurseIndex = ThreadLocalRandom.current().nextInt(0, amountOfNurses);
            Patient patient = getLowestPatient(copyOfPatients);
            Nurse nurse = listOfNurses.get(randomNurseIndex);
            nurse.addListOfPatients(patient);
            boolean isValid = checkValidity(nurse, patient, counter);

            while (!isValid && counter < 100) {
                nurse.removeListOfPatients(patient);
                randomNurseIndex = ThreadLocalRandom.current().nextInt(0, amountOfNurses);
                nurse = listOfNurses.get(randomNurseIndex);
                nurse.addListOfPatients(patient);
                isValid = checkValidity(nurse, patient, counter);
                counter++;

                if (counter == 99 && !isValid) {
                    nurse.removeListOfPatients(patient);
                }
            }
            if (isValid) {
                nurse.setNurse_traveled(computeTravelDistance(nurse, false) + patient.getCareTime());
                nurse.setTime_traveled(computeTravelDistance(nurse, false));
                nurse.setCapacity(patient.getDemand());
                copyOfPatients.remove(patient);
            }
        }
        for (Nurse n : listOfNurses) {
            n.setNurse_traveled(computeTravelDistance(n, true));
            n.setTime_traveled(computeTravelDistance(n, true));
        }
        return listOfNurses;
    }

    public List<Nurse> generateGreedyInd(List<Patient> patients, int amountOfNurses) {
        List<Patient> copyOfPatients = new ArrayList<>(patients);
        Depot depot = new Depot();
        List<Nurse> nurses = depot.getAvailable_nurses();
        Map<Nurse, Integer> nursePos = new HashMap<>();
        for (int i = 0; i < nurses.size(); i++) {
            nursePos.put(nurses.get(i), 0);
        }
        while (copyOfPatients.size() != 0) {
            int randomPatientIndex = ThreadLocalRandom.current().nextInt(0, copyOfPatients.size()); //eller bruke getLowestPatient()?
            Patient patient = copyOfPatients.remove(randomPatientIndex);
            Nurse n = null;

            List<Nurse> nursesChecked = new ArrayList<>();
            double distance = 10000;
            int closestNurse = 0;
            for (Nurse nurse : nursePos.keySet()) {
                int pos = nursePos.get(nurse);
                double travelTime = Settings.travelMatrix.get(pos).get(patient.getId());
                if (travelTime < distance) {
                    distance = travelTime;
                    closestNurse = nurse.getId();
                    n = nurses.get(closestNurse);
                }
            }
            n.addListOfPatients(patient);
            nursesChecked.add(n);
            boolean valid = checkValidity(nurses);
        }
        return null;
    }

    public boolean checkValidity(List<Nurse>nurses) {
        for(Nurse n : nurses){
            int lastStop = 0;
            int capacityUsed = 0;
            double totalTime = 0;

            if(n.getListOfPatients().size() != 0){
                for(Patient p : n.getListOfPatients()){
                    totalTime += Settings.travelMatrix.get(lastStop).get(p.getId());
                    if(totalTime < p.getStartWindow()){
                        totalTime = p.getStartWindow();
                    }
                    totalTime+=p.getCareTime();
                    if(p.getEndWindow() < totalTime){
                        return false;
                    }
                    capacityUsed += p.getDemand();
                    if(capacityUsed > n.getCapacity()){
                        return false;
                    }
                    lastStop = p.getId();
                }
                //Back to depot
                totalTime+= Settings.travelMatrix.get(0).get(lastStop);
                if(Settings.depot_return_time > totalTime){
                    return false;
                }
            }

        }
        return true;
    }



    public Patient getLowestPatient(List<Patient>patients){
        Patient holder = patients.get(0);
        for(int i = 1; i < patients.size(); i++){
            if(holder.getEndWindow() == 0) {
                return holder;
            }
            if(holder.getEndWindow() > patients.get(i).getEndWindow()){
                holder = patients.get(i);
            }
        }
        return holder;
    }
    public Patient getLowestInterval(List<Patient> patients){
        Patient holder = patients.get(0);
        for(int i = 1; i < patients.size(); i++) {
            if (holder.getEndWindow() - holder.getStartWindow() > patients.get(i).getEndWindow() - patients.get(i).getStartWindow()) {
                holder = patients.get(i);
            }
        }
        return holder;
    }


    public double computeTravelDistance(Nurse nurse, boolean back) {
        List<Patient> listOfPatients = nurse.getListOfPatients();
        Patient patient = listOfPatients.get(0);
        int nurseId = nurse.getId();
        double traveled;

        if(back){
            int fromPatient = 0;
            if(listOfPatients.size()==1){
                traveled = Settings.travelMatrix.get(nurseId).get(0);
            }else{
                fromPatient = listOfPatients.get(listOfPatients.size()-1).getId();
                traveled = Settings.travelMatrix.get(fromPatient).get(0);
            }
            return traveled;
        }

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
