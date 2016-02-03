/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven
 */
public class Team {
    private int teamID;
    private int nextTeam;
    private int prevTeam;
    private int cycle;
    private int cyclePosition;
    private String teamName;
    
    public Team(String name, int id, int next, int prev, int cycle, int position){
        this.teamID = id;
        this.nextTeam = next;
        this.prevTeam = prev;
        this.cycle = cycle;
        this.cyclePosition = position;
        this.teamName = name;
    }
    public Team(){
        this.teamID = 0;
        this.nextTeam = 0;
        this.prevTeam = 0;
        this.cycle = 0;
        this.cyclePosition = 0;
        this.teamName = null;
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
}
