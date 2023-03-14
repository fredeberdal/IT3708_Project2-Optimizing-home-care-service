package models;

import java.util.List;

public class Nurse {

    private final int id;
    private int capacity;

    private double nurse_traveled = 0.0;

    private List<Patient> listOfPatients;

    public Nurse (int id, int capacity, List<Patient> listOfPatients) {
        this.id = id;
        this.capacity = capacity;
        this.listOfPatients = listOfPatients;
    }

    public int getId() { return id; }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int demand) {
        this.capacity -= demand;
    }

    public double getNurse_traveled() {
        return nurse_traveled;
    }
    public void setNurse_traveled(double nurse_traveled) {
        this.nurse_traveled += nurse_traveled;
    }

    public List<Patient> getListOfPatients() {
        return listOfPatients;
    }
    public void addListOfPatients(Patient patient) {
        this.listOfPatients.add(patient);
    }
    public void removeListOfPatients(Patient patient) {
        this.listOfPatients.remove(patient);
    }

}

