
package footballschedulingv2;
public class Division {
    private String divisionName;
    private int id;
    private Team[] schedule;
    private Cycle[] cycles;
    private double[][] distances;  
    private int highestCycle;
    
    public Division(int id,String name, Team[] schedule, Cycle[] cycles, double[][] distances){
        this.schedule = schedule;
        this.cycles = cycles;
        this.distances = distances;
        this.highestCycle = 0;
        this.divisionName = name;
        this.id = id;
    }
    public Division(Division d){
        id = d.id;
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
     * @return the cycles
     */
    public Cycle[] getCycles() {
        return cycles;
    }
    /**
     * @return the distances
     */
    public double[][] getDistances() {
        return distances;
    }
    /**
     * @return the highestCycle
     */
    public int getHighestCycle() {
        return highestCycle;
    }
    /**
     * @return the divisionName
     */
    public void setHighestCycle(int a){
        highestCycle = a;
    }
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

}
