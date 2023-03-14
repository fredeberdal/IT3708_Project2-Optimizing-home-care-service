package models;

import java.util.ArrayList;
import java.util.List;

public class Depot {

    private int maxDuration;
    private List<Nurse> available_nurses;

    public Depot () {
        this.maxDuration = Settings.depot_return_time;
        generateListOfNurses();
    }

    public int getMaxDuration() { return maxDuration; }
    public void setMaxDuration(int maxDuration) { this.maxDuration = maxDuration; }

    public List<Nurse> getAvailable_nurses() { return available_nurses; }
    public void setAvailable_nurses(List<Nurse> available_nurses) { this.available_nurses = available_nurses; }

    public void generateListOfNurses() {
        List<Nurse> nurses = new ArrayList<Nurse>();
        for (int i = 0; i<Settings.number_of_nurses; i++) {
            List<Patient> patients = new ArrayList<Patient>();
            nurses.add(new Nurse(i, Settings.nurse_capacity, patients));
        }
        available_nurses = nurses;
    }
}
