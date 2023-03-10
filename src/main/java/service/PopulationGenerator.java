package service;

import models.Patient;

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

    public boolean checkValidity(List<List<Integer>>visitList, Patient patient, int nurse) {
        List<Integer> nurseList = visitList.get(nurse);
    }

    public void generateRandom(List<Patient> patients, int amountOfNurses){
        List<List<Integer>> listOfVisits = listInitializer(amountOfNurses);
        List<Patient> patientsSecond = new ArrayList<>(patients);
        while(patientsSecond.size() != 0){
            int randomIndex = ThreadLocalRandom.current().nextInt(0, patientsSecond.size());
            int randomNurse = ThreadLocalRandom.current().nextInt(0, amountOfNurses);
            Patient patient = patientsSecond.remove(randomIndex);

            listOfVisits.get(randomNurse).add(patient.getId());

        }
    }

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
}
