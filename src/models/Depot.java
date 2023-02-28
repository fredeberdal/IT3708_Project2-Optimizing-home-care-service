package models;

import java.util.List;

public class Depot {

    private int maxDuration;
    private int total_travel_time; // Kan være i Nurse klassen istedet? Lettere å holde styr på?
    private List<Nurse> available_nurses;

    public Depot (int total_travel_time, List<Nurse> available_nurses, int maxDuration) {
        this.total_travel_time = total_travel_time;
        this.available_nurses = available_nurses;
        this.maxDuration = maxDuration;

    }

    public int getTotal_travel_time() {
        return total_travel_time;
    }
    public void setTotal_travel_time(int total_travel_time) {
        this.total_travel_time = total_travel_time;
    }

    public int getMaxDuration() { return maxDuration; }
    public void setMaxDuration(int maxDuration) { this.maxDuration = maxDuration; }

    public List<Nurse> getAvailable_nurses() { return available_nurses; }
    public void setAvailable_nurses(List<Nurse> available_nurses) { this.available_nurses = available_nurses; }
}
