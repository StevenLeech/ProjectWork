/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;

import java.util.ArrayList;

public class Team {
    private int teamID;
    private int nextTeam;
    private int prevTeam;
    private int cycle;
    private int cyclePosition;
    private String teamName;
    private ArrayList<String> pairs;
    private int location;
    private int division;
    
    public Team(String name, int id, int next, int prev, int cycle, int position,ArrayList<String> pairs,int location,int division){
        this.teamID = id;
        this.nextTeam = next;
        this.prevTeam = prev;
        this.cycle = cycle;
        this.cyclePosition = position;
        this.teamName = name;
        this.pairs = pairs;
        this.location = location;
        this.division = division;
    }
    public Team(){
        this.teamID = 0;
        this.nextTeam = 0;
        this.prevTeam = 0;
        this.cycle = 0;
        this.cyclePosition = 0;
        this.teamName = null;
        this.pairs = new ArrayList<String>();
        this.location=0;
        this.division=0;
    }
    public Team(Team t){
        teamID = t.teamID;
        nextTeam = t.nextTeam;
        prevTeam = t.prevTeam;
        cycle = t.cycle;
        cyclePosition = t.cyclePosition;
        teamName = t.teamName;
        location = t.location;
        division = t.division;
        pairs = new ArrayList<String>();
        for(int i=0;i<t.pairs.size();i++){
            String str = t.pairs.get(i);
            pairs.add(str);
        }
    }
   

    /**
     * @return the teamID
     */
    public int getTeamID() {
        return teamID;
    }

    /**
     * @param teamID the teamID to set
     */
    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    /**
     * @return the nextTeam
     */
    public int getNextTeam() {
        return nextTeam;
    }

    /**
     * @param nextTeam the nextTeam to set
     */
    public void setNextTeam(int nextTeam) {
        this.nextTeam = nextTeam;
    }

    /**
     * @return the prevTeam
     */
    public int getPrevTeam() {
        return prevTeam;
    }

    /**
     * @param prevTeam the prevTeam to set
     */
    public void setPrevTeam(int prevTeam) {
        this.prevTeam = prevTeam;
    }

    /**
     * @return the cycle
     */
    public int getCycle() {
        return cycle;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    /**
     * @return the cyclePosition
     */
    public int getCyclePosition() {
        return cyclePosition;
    }

    /**
     * @param cyclePosition the cyclePosition to set
     */
    public void setCyclePosition(int cyclePosition) {
        this.cyclePosition = cyclePosition;
    }

    /**
     * @return the teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName the teamName to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return the pairs
     */
    public ArrayList<String> getPairs() {
        return pairs;
    }

    /**
     * @param pairs the pairs to set
     */
    public void setPairs(ArrayList<String> pairs) {
        this.pairs = pairs;
    }

    /**
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(int location) {
        this.location = location;
    }

    /**
     * @return the division
     */
    public int getDivision() {
        return division;
    }

    /**
     * @param division the division to set
     */
    public void setDivision(int division) {
        this.division = division;
    }
   
    
}
