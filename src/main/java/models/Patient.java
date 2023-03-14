package models;

public class Patient {

    private final int id;
    private final int startWindow;
    private final int endWindow;
    private final int careTime;
    private final int demand;
    private final int xCordinate;
    private final int yCordinate;

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

    public int getEndWindow() {
        return endWindow;
    }

    public int getCareTime() {
        return careTime;
    }

    public int getDemand() {
        return demand;
    }

    public int getId() { return id; }

    public int getxCordinate() { return xCordinate; }
    public int getyCordinate() { return yCordinate; }
}
