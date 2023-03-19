package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import models.Nurse;
import models.Patient;
import models.Depot;
import models.Settings;

public class Population {
    public List<Individual>individuals = new ArrayList<>();

    public Population(){

    }
    public Population makePopulation(){
        Population population = new Population();
        for(int i = 0; i<Settings.POP_SIZE; i++){
            Individual ind = new Individual();
            ind.makeIndividual(ind);
            population.individuals.add(ind);
        }
        return population;
    }

    public Population generateNewGen(Population pop){
        pop.eliteDelete();

        Population children = new Population();
        Pair childrenPair;
        for(int i = 0; i<pop.individuals.size();i++){
            childrenPair = crossover(pop.parentsSelectionElite());
            childrenPair.parent1.fitness = childrenPair.parent1.calculateFitness(childrenPair.parent1.nurses);
            childrenPair.parent2.fitness = childrenPair.parent2.calculateFitness(childrenPair.parent2.nurses);
            children.individuals.add(childrenPair.parent1);
            children.individuals.add(childrenPair.parent2);
        }

        for(Individual ind : children.individuals){
            pop.individuals.add(ind);
        }
        return pop;
    }
    public void sortPop(){
        this.individuals.sort(Comparator.comparing(individual -> individual.fitness));
    }
    public void print(){
        for(Individual ind : this.individuals){
            System.out.println(ind.fitness);
        }
    }
    public Pair parentsSelectionElite(){
        int random = (int)Math.floor(Math.random() * (10));
        //System.out.println(random);
        Individual p1 = this.individuals.get(random);
        Individual p2 = this.individuals.get(random);
        Pair pair = new Pair(p1, p2);
        return pair;
    }

    public Pair crossover(Pair parents){
        Individual p1 = parents.parent1;
        Individual p2 = parents.parent2;

        Pair children;
        children = routeCrossover(parents);
        Individual c1 = children.parent1;
        Individual c2 = children.parent2;

        //Mutate
        c1 = mutate(c1);
        c2 = mutate(c2);

        //Crowding if new children are valid
        //System.out.println(c1.checkValidity(c1.nurses));
        if(c1 != null && c2 != null && p1 != null && p2 != null){
            return crowding(c1,c2,p1,p2);
        }else{
            return parents;
        }
        /*
        if(c1.checkValidity(c1.nurses) && c2.checkValidity(c2.nurses)){
            return crowding(c1, c2, p1, p2);
        }else{
            System.out.println("ikke lovlig");
        }

         */
        //children.parent1 = c1;
        //children.parent2 = c2;
        //return children;
    }
    public Individual compareChrom(Individual ind1, Individual ind2){
        Individual best;
        if(ind1.fitness > ind2.fitness){
            best = ind2;
        }else{
            best = ind1;
        }
        return best;
    }

    public Pair crowding(Individual c1, Individual c2, Individual p1, Individual p2) {
        int hammingc1p1 = hamming(c1, p1);
        int hammingc1p2 = hamming(c1, p2);
        int hammingc2p1 = hamming(c2, p1);
        int hammingc2p2 = hamming(c2, p2);
        Individual best1, best2;
        if(hammingc1p2 + hammingc2p1<hammingc1p1 + hammingc2p2){
            best1 = compareChrom(p1, c1);
            best2 = compareChrom(p2,c2);
        }else{
            best1 = compareChrom(p1, p2);
            best2 = compareChrom(p2, p1);
        }
        Pair pair = new Pair(best1, best2);
        return pair;
    }

    public int hamming(Individual ind1, Individual ind2){
        int counter = 0;
        List<Patient> patient1 = ind1.patientsFlat();
        List<Patient> patient2 = ind2.patientsFlat();
        for(int i = 0; i<patient1.size(); i++){
            if(patient1.get(i) == patient2.get(i)){
                counter++;
            }
        }
        return counter;
    }


    public Individual mutatePatientBest(Individual individual){
        return null;
    }
    public Individual mutate(Individual individual){
        double rand = ThreadLocalRandom.current().nextDouble();
        if(rand > Settings.mutationProb){
            return individual;
        }
        individual = mutatePatientBest(individual);
        return individual;
    }

    public Pair routeCrossover(Pair parents) {
        Individual p1 = parents.parent1;
        Individual p2 = parents.parent2;
        Nurse nurseChild1 = findNurseRoute(p1, 1);
        Nurse nurseChild2 = findNurseRoute(p2, 1);
        Individual child1 = removePatients(nurseChild1, p2);
        Individual child2 = removePatients(nurseChild2, p1);

        //Individual child1 = removePatients(nurseChild1, p2);
        //Individual child2 = removePatients(nurseChild2, p1);

        child1 = insertRandom(child1, nurseChild2);
        child2 = insertRandom(child2, nurseChild1);
        //Maybe a loop to check if it has gone wrong
        Pair pair = new Pair(child1, child2);

        return pair;
    }
    public Individual insertRandom(Individual child, Nurse nurseChild){
        List<Patient> patients = nurseChild.getListOfPatients();
        int random;
        while(!nurseChild.getListOfPatients().isEmpty()){
            random = ThreadLocalRandom.current().nextInt(0, Settings.number_of_nurses);
            child.nurses.get(random).addListOfPatients(nurseChild.getListOfPatients().get(0));
            nurseChild.removeListOfPatients(nurseChild.getListOfPatients().get(0));
        }
        return child;
        }

    public Individual mutateTwoNurses(Individual child){

        Nurse n1 = findNurseRoute(child, 1);
        Nurse n2 = findNurseRoute(child, 1);
        int randPatient1 = ThreadLocalRandom.current().nextInt(0, n1.getListOfPatients().size());
        int randPatient2 = ThreadLocalRandom.current().nextInt(0, n2.getListOfPatients().size());
        Patient p1 = n1.getListOfPatients().get(randPatient1);
        Patient p2 = n2.getListOfPatients().get(randPatient2);

        child.nurses.get(n1.getId()).replacePatient(p1, randPatient1);
        child.nurses.get(n1.getId()).replacePatient(p2, randPatient2);

        return child;
        /*
        List<Patient> patients = nurseChild.getListOfPatients();

        Individual newChild;
        for(int i = 0; i<child.nurses.size();i++){
            for(int j = 0; j<child.nurses.get(i).getListOfPatients().size();j++){
                child.nurses.get(i).getListOfPatients().add(patients.)
            }
        }

         */
    }

    public Individual removePatients(Nurse nurseChild2, Individual p1) {
        Individual individualNew = new Individual(p1);
        for(Patient p : nurseChild2.getListOfPatients()){
            individualNew = removePatient(p, p1);
        }
        return individualNew;
    }
    public Individual removePatient(Patient patient, Individual p1) {
        Individual individualNew = new Individual(p1);
        for(Nurse n : p1.nurses){
            for(Patient p : n.getListOfPatients()){
                if(patient == p){
                    individualNew.nurses.get(n.getId()).removeListOfPatients(p);
                    return individualNew;
                }
            }
        }

        return individualNew;
    }

    public Nurse findNurseRoute(Individual ind, int amountOfPatients){
        List<Nurse> nursesWithPatients = new ArrayList<>();
        for(Nurse n : ind.nurses){
            if(n.getListOfPatients().size() > amountOfPatients-1){
                nursesWithPatients.add(n);
            }
        }
        Nurse n = ind.nurses.get(0);
        if(nursesWithPatients != null){
            int random = ThreadLocalRandom.current().nextInt(0, nursesWithPatients.size());
            n = nursesWithPatients.get(random);
        }
        return n;
    }

    public void eliteDelete(){
        this.sortPop();
        this.individuals.subList(this.individuals.size()/2, this.individuals.size()).clear();
    }



    /*
    public double roulette_wheel_selection(Population pop){
        double max = 0;
        double prob = 0;
        List<Individual> choices = new ArrayList<>();
        for(Individual ind : pop.individuals){
            max += ind.fitness;
        }
        for(Individual ind : pop.individuals){
            prob = ind.fitness / max;
        }
    }

     */
}


