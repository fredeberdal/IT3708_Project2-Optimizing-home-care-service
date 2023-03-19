package service;

import models.Nurse;
import models.Patient;
import models.Depot;
import models.Settings;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Individual {
    //public HashMap<List<Nurse>, Double> tup = new HashMap<List<Nurse>, Double>();
    public boolean failed = false;
    public List<Nurse> nurses;
    public double fitness;

    public Individual(){
    }
    public Individual makeIndividual(Individual ind) {
        ind.nurses = generateGreedyInd(Settings.patients, Settings.number_of_nurses);
        ind.fitness = calculateFitness(nurses);
        return ind;
    }

    public Individual(Individual ind){
        this.nurses = ind.nurses;
        this.fitness = ind.fitness;
    }

    public double calculateFitness(List<Nurse> nurses){
        double fitness = 0;
        double pen = 0;
        int penalty = 1000;
        double timeViolation = 0.0;
        double totalTravelTime = 0.0;
        for(Nurse nurse : nurses){
            totalTravelTime += nurse.getTime_traveled();
            if(nurse.getNurse_traveled() > Settings.depot_return_time){
                timeViolation += nurse.getNurse_traveled() - Settings.depot_return_time;
            }
        }
        if(this.failed){
            pen = 2;
            System.out.println("failed");
        }
        fitness = totalTravelTime + (timeViolation * penalty) + (penalty*pen);
        return fitness;
    }
    public double calculateRoute(Nurse nurse){
        double totalTravelTime = 0;
        int p=0;
        //System.out.println(nurse.getListOfPatients().size());
        for(int i = 0; i<nurse.getListOfPatients().size(); i++){
            if(i == nurse.getListOfPatients().size()-1){
                totalTravelTime += Settings.travelMatrix.get(0).get(nurse.getListOfPatients().get(i).getId());
            }
        }
        return totalTravelTime;
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
            if(patient.getStartWindow()>0){
                nurse.setNurse_traveled(patient.getStartWindow());
                return true;
            }
            return true;
        }
        if (counter > 50 && nurse.getCapacity() > patient.getDemand() && nurse.getNurse_traveled() < patient.getStartWindow()) { //wait to first window if needed
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
                d++;
                //System.out.println(d);
                nurse.setNurse_traveled(computeTravelDistance(nurse, false) + patient.getCareTime());
                nurse.setTime_traveled(computeTravelDistance(nurse, false));
                nurse.setCapacity(nurse.getCapacity() - patient.getDemand());
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
            if (patients.size() - 1 == copyOfPatients.size()) {
                n = nurses.get(0);
                nursePos.put(n, patient.getId());
                n.addListOfPatients(patient);
            } else {
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
                while (amountOfNurses > nursesChecked.size() && !valid) {
                    n.removeListOfPatients(patient);
                    distance = 10000;
                    closestNurse = 0;
                    for (Nurse nurse : nursePos.keySet()) {
                        int pos = nursePos.get(nurse);
                        double travelTime = Settings.travelMatrix.get(pos).get(patient.getId());
                        if (travelTime < distance && !(nursesChecked.contains(nurse))) {
                                distance = travelTime;
                                closestNurse = nurse.getId();
                                n = nurses.get(closestNurse);
                            }
                        }
                    n.addListOfPatients(patient);
                    nursesChecked.add(n);
                    valid = checkValidity(nurses);
                }
                if (!valid) {
                    //System.out.println("Her feilet jeg ");
                    n.removeListOfPatients(patient);
                    for (Nurse nurse : nursePos.keySet()) {
                        int pos = nursePos.get(nurse);
                        double travelTime = Settings.travelMatrix.get(pos).get(patient.getId());
                        if (travelTime < distance) {
                            distance = travelTime;
                            closestNurse = nurse.getId();
                            n = nurses.get(closestNurse);
                        }
                    }
                    this.failed = true;
                    n.addListOfPatients(patient);
                }
                nursePos.put(n, patient.getId());
            }
        }
        return nurses;
    }

    public Nurse findClosestNurse(Patient patient, List<Nurse>nurses, Map<Nurse, Integer> nursePos, List<Nurse>checkedNurses, boolean checked) {
        double distance = 10000;
        int closestNurse = 0;
        Nurse n = null;
        for (Nurse nurse : nursePos.keySet()) {
            int pos = nursePos.get(nurse);
            double travelTime = Settings.travelMatrix.get(pos).get(patient.getId());
            if (checked) {
                if (travelTime < distance) {
                    distance = travelTime;
                    closestNurse = nurse.getId();
                    n = nurses.get(closestNurse);
                }
            } else {

                if (travelTime < distance && !(checkedNurses.contains(nurse))) {
                    distance = travelTime;
                    closestNurse = nurse.getId();
                    n = nurses.get(closestNurse);
                }
            }

        }
        return n;
    }

    public boolean checkValidity(List<Nurse>nurses) {
        for(Nurse n : nurses){
            int lastStop = 0;
            int capacityUsed = 0;
            double totalTime = 0;
            double totalTravelTime = 0;

            if(n.getListOfPatients().size() != 0){
                for(Patient p : n.getListOfPatients()){
                    totalTime += Settings.travelMatrix.get(lastStop).get(p.getId());
                    totalTravelTime += Settings.travelMatrix.get(lastStop).get(p.getId());
                    if(totalTime < p.getStartWindow()){
                        totalTime = p.getStartWindow();
                    }
                    totalTime+=p.getCareTime();
                    if(p.getEndWindow() < totalTime){
                        //System.out.println("Failed at endwindow");
                        return false;
                    }
                    capacityUsed += p.getDemand();
                    if(capacityUsed > n.getCapacity()){
                        System.out.println("Failed at capacity");
                        return false;
                    }
                    lastStop = p.getId();
                }
                //Back to depot
                totalTime += Settings.travelMatrix.get(0).get(lastStop);
                totalTravelTime += Settings.travelMatrix.get(0).get(lastStop);
                if(Settings.depot_return_time < totalTime){
                    System.out.println("Failed at returntime");
                    return false;
                }
            }
            n.setNurse_traveled(totalTime);
            //n.setCapacity(capacityUsed);
            n.setTime_traveled(totalTravelTime);
        }
        return true;
    }

    public List<Patient> patientsFlat(){
        List<Patient> patientsFlat = new ArrayList<>();
        for(Nurse n : this.nurses){
            for(Patient p : n.getListOfPatients()){
                patientsFlat.add(p);
            }
        }
        return patientsFlat;
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
        if(listOfPatients.isEmpty()){
            return 0;
        }
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
