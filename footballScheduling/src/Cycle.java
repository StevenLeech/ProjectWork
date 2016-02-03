
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven
 */
public class Cycle {
    private int cycleId;
    
    private ArrayList<Team> teams;
    
    public Cycle(int id){
        this.cycleId = id;
        this.teams = new ArrayList<Team>();
        
    }

    /**
     * @return the cycleId
     */
    public int getCycleId() {
        return cycleId;
    }

    /**
     * @param cycleId the cycleId to set
     */
    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    /**
     * @return the teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
    public void addTeam(Team team){
        this.teams.add(team);
    }
    public void clearList(){
        this.teams.clear();
    }
    public int getSize(){
        return this.teams.size();
    }
    public void removeTeam(Team team){
        this.teams.remove(team);
    }
    /**
     * @return the cycleSize
     */
    public int getCycleSize() {
        return teams.size();
    }

    /**
     * @param cycleSize the cycleSize to set
     */
    
}
