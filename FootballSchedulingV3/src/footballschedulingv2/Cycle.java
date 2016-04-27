/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;
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
    
    public Cycle(Cycle c,Team[] s){
        cycleId = c.cycleId;
        teams = new ArrayList<>();
        for(int i=0;i<c.teams.size();i++){
            Team team = s[c.teams.get(i).getTeamID()];
            teams.add(team);
        }
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
    
    public boolean isEmpty(){
        if(teams.size()==0){
            return true;
        }
        else{
            return false;
        }
    }
    
}
