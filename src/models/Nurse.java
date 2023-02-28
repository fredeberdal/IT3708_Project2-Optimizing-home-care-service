package models;

public class Nurse {

    private int id;
    private int capacity;
    private int current_route; // Burde være en liste.

    public Nurse (int id, int capacity, int current_route) {
        this.id = id;
        this.capacity = capacity;
        this.current_route = current_route;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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
}
