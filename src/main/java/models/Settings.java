package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Settings {

    public static int TASK_NUMBER = 0;
    public static int POP_SIZE = 100;
    public static int nurse_capacity;
    public static int number_of_nurses;
    public static int depot_return_time;

    public static double mutationProb = 0.2;
    public static double max_travel_time = 0;
    public static List<Patient> patients = new ArrayList<>();

    public static LinkedHashMap<List<Nurse>, Double> individuals = new LinkedHashMap<List<Nurse>, Double>();

    public static List<List<Double>> travelMatrix;

    public static void setTravelMatrix(List<List<Double>> travelMatrix) {
        Settings.travelMatrix = travelMatrix;
    }

    public static void setPatients(List<Patient> patients) {
        Settings.patients = patients;
    }

    public static void setNurse_capacity(int nurse_capacity) {
        Settings.nurse_capacity = nurse_capacity;
    }

    public static void setNumber_of_nurses(int number_of_nurses) {
        Settings.number_of_nurses = number_of_nurses;
    }

    public static void setDepot_return_time(int depot_return_time) {
        Settings.depot_return_time = depot_return_time;
    }

    public static void setTotal_travel_time(double travel_time) {
        Settings.max_travel_time = max_travel_time + travel_time;
    }

}
