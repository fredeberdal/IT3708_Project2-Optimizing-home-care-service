package models;

public class Patient {

    private int id;
    private int startWindow;
    private int endWindow;
    private int careTime;
    private int demand;
    private int xCordinate;
    private int yCordinate;

    public Patient(int id, int startWindow, int endWindow, int careTime, int demand, int xCordinate, int yCordinate){
        this.id = id;
        this.startWindow = startWindow;
        this.endWindow = endWindow;
        this.careTime = careTime;
        this.demand = demand;
        this.xCordinate = xCordinate;
        this.yCordinate = yCordinate;
    }

    public int getStartWindow() {
        return startWindow;
    }

    public void setStartWindow(int startWindow) {
        this.startWindow = startWindow;
    }

    public int getEndWindow() {
        return endWindow;
    }

    public void setEndWindow(int endWindow) {
        this.endWindow = endWindow;
    }

    public int getCareTime() {
        return careTime;
    }

    public void setCareTime(int careTime) {
        this.careTime = careTime;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getxCordinate() { return xCordinate; }

    public void setxCordinate(int xCordinate) { this.xCordinate = xCordinate; }

    public int getyCordinate() { return yCordinate; }

    public void setyCordinate(int yCordinate) { this.yCordinate = yCordinate; }
}
