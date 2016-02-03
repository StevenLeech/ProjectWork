/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven
 */
public class Division {
    private String divisionName;
    private Team[] schedule;
    private Cycle[] cycles;
    private int[][] distances;  
    private int highestCycle;
    
    public Division(String name, Team[] schedule, Cycle[] cycles, int[][] distances){
        this.schedule = schedule;
        this.cycles = cycles;
        this.distances = distances;
        this.highestCycle = 0;
        this.divisionName = name;
    }

    /**
     * @return the schedule
     */
    public Team[] getSchedule() {
        return schedule;
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(Team[] schedule) {
        this.schedule = schedule;
    }

    /**
     * @return the cycles
     */
    public Cycle[] getCycles() {
        return cycles;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setCycles(Cycle[] cycles) {
        this.cycles = cycles;
    }

    /**
     * @return the distances
     */
    public int[][] getDistances() {
        return distances;
    }

    /**
     * @param distances the distances to set
     */
    public void setDistances(int[][] distances) {
        this.distances = distances;
    }

    /**
     * @return the highestCycle
     */
    public int getHighestCycle() {
        return highestCycle;
    }

    /**
     * @param highestCycle the highestCycle to set
     */
    public void setHighestCycle(int highestCycle) {
        this.highestCycle = highestCycle;
    }

    /**
     * @return the divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName the divisionName to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}
