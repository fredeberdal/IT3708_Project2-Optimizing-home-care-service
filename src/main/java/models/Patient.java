package models;

public class Patient {

    private int startWindow;
    private int endWindow;
    private int careTime;
    private int demand;

    public Patient(int startWindow, int endWindow, int careTime, int demand){
        this.startWindow = startWindow;
        this.endWindow = endWindow;
        this.careTime = careTime;
        this.demand = demand;
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
}
