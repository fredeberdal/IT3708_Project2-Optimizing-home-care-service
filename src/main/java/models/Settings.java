package models;

import java.util.List;

public class Settings {

    public static int TASK_NUMBER = 0;
    public static int nurse_capacity;
    public static int number_of_nurses;
    public static int depot_return_time;

    public static List<List<Double>> travelMatrix;

    public static void setTravelMatrix(List<List<Double>> travelMatrix) {
        Settings.travelMatrix = travelMatrix;
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
}
