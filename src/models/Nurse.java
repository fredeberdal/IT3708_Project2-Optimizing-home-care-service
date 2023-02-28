package models;

public class Nurse {

    private int id;
    private int capacity;
    private int current_route;
    private int total_travel_time;

    public Nurse (int id, int capacity, int current_route, int total_travel_time) {
        this.id = id;
        this.capacity = capacity;
        this.current_route = current_route;
        this.total_travel_time = total_travel_time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrent_route() {
        return current_route;
    }
    public void setCurrent_route(int current_route) {
        this.current_route = current_route;
    }

    public int getTotal_travel_time() {
        return total_travel_time;
    }
    public void setTotal_travel_time(int total_travel_time) {
        this.total_travel_time = total_travel_time;
    }
}
