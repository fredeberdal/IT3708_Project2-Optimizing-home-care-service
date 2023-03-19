package service;

import models.Nurse;
import models.Patient;
import models.Depot;
import models.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PopulationGenerator {
    public boolean checkValidity(Nurse nurse, Patient patient, int counter) {
        if (nurse.getListOfPatients().size() == 1) {
            return true;
        }

        if (counter > 20 && nurse.getCapacity() > patient.getDemand() && nurse.getNurse_traveled() < patient.getStartWindow()) {
            nurse.setNurse_traveled(patient.getStartWindow()-nurse.getNurse_traveled());
        }

        if (nurse.getNurse_traveled() <= patient.getEndWindow()-patient.getCareTime() && nurse.getNurse_traveled() >= patient.getStartWindow()) {
            if (nurse.getCapacity()-patient.getDemand() > 0) {
                System.out.println("demand");
                if(nurse.getNurse_traveled() + computeTravelDistance(nurse) + patient.getCareTime() < Settings.depot_return_time) {
                    System.out.println("Is Valid");
                    return true;
                    //if(nurse.getNurse_traveled() + computeTravelDistance(nurse) + patient.getCareTime() < Settings.depot_return_time) {
                    // Fjerna for å heller legge på penalty på fitness om man er over depot return time slik at den skal lære
                    // TODO Pass på at pasientene til samme nurse ikke overlapper (caretime går over neste patient endWindow
                }
            }
        }
        return false;
    }
    public List<Nurse> generateRandom(List<Patient> patients, int amountOfNurses){
        List<Patient> copyOfPatients = new ArrayList<>(patients);
        Depot depot = new Depot();
        int d = 0;
        List<Nurse> listOfNurses = depot.getAvailable_nurses();
        while(!copyOfPatients.isEmpty()){
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
                nurse.setNurse_traveled(computeTravelDistance(nurse) + patient.getCareTime());
                nurse.setCapacity(patient.getDemand());
                nurse.setTime_traveled(computeTravelDistance(nurse));
                copyOfPatients.remove(patient);
            }
        }
        return listOfNurses;
    }
/*
    public void generateRandomIndividual(int nurses, List<Patient> patients) {
        List<List<Integer>> visitList = this.initEmptyNurseRoutes(nurses);
        List<Patient> patientsCopy = new ArrayList<>(patients);

        while(!patientsCopy.isEmpty()){
            int ind = ThreadLocalRandom.current().nextInt(0, patientsCopy.size());
            Patient patient = patientsCopy.remove(ind);
            if(patient != null) {
                int patientId = patient.getPatient_id();
                int randomNurseId = ThreadLocalRandom.current().nextInt(0, nurses);

                visitList.get(randomNurseId).add(patientId);
                this.patientNurseMap.put(patientId, randomNurseId);
                boolean valid = checkValidityOfIndividual(visitList);
                int counter = 0;
                // Make it so that if there is no valid combination, due to earlier
                // problems we don't get stuck in this loop
                while(!valid && (counter < 100)){
                    visitList.get(randomNurseId).remove((visitList.get(randomNurseId).size() - 1));
                    randomNurseId = ThreadLocalRandom.current().nextInt(0, nurses);
                    visitList.get(randomNurseId).add(patient.getPatient_id());
                    valid = checkValidityOfIndividual(visitList);
                    counter++;
                }
            }
        }
        this.is_valid = checkValidityOfIndividual(visitList);

        this.nurseRoutes = visitList;
    }

 */


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

    public double computeTravelDistance(Nurse nurse) {
        List<Patient> listOfPatients = nurse.getListOfPatients();
        Patient patient = listOfPatients.get(0);
        int nurseId = nurse.getId();
        double traveled;

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
