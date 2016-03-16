
package footballschedulingv2;
public class Division {
    private String divisionName;
    private Team[] schedule;
    private Cycle[] cycles;
    private double[][] distances;  
    private int highestCycle;
    
    public Division(String name, Team[] schedule, Cycle[] cycles, double[][] distances){
        this.schedule = schedule;
        this.cycles = cycles;
        this.distances = distances;
        this.highestCycle = 0;
        this.divisionName = name;
    }
    public Division(Division d){
        divisionName = d.divisionName;
        schedule = new Team[d.schedule.length];
        for(int i=0; i<d.schedule.length;i++){
            schedule[i] = new Team(d.schedule[i]);
        }
        cycles = new Cycle[d.cycles.length]; 
        for(int i=0;i<d.cycles.length;i++){
            cycles[i]= new Cycle(d.cycles[i],schedule);
        }
        distances = new double[d.distances.length][d.distances[0].length];
        for(int i=0;i<d.distances.length;i++){
            for(int j=0;j<d.distances[i].length;j++){
                distances[i][j] = d.distances[i][j];
            }
        }
        highestCycle = d.highestCycle;
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
    public double[][] getDistances() {
        return distances;
    }

    /**
     * @param distances the distances to set
     */
    public void setDistances(double[][] distances) {
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
