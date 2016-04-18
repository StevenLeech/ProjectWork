
package footballschedulingv2;
public class Fixture {
    private Team homeTeam;
    private Team awayTeam;
    private String clash;
    private double distance;
    
    public Fixture(Team home, Team away){
        this.homeTeam = home;
        this.awayTeam = away;
        this.clash= " ";
        this.distance=0;
    }
    public Fixture(Fixture f){
        homeTeam = new Team(f.homeTeam);
        awayTeam = new Team(f.awayTeam);
        clash = f.clash;
        this.distance = f.distance;
    }
    
    /**
     * @return the homeTeam
     */
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * @return the awayTeam
     */
    public Team getAwayTeam() {
        return awayTeam;
    }
    
    @Override
    public String toString(){
        //String output = homeTeam.getTeamName() + " vs " + awayTeam.getTeamName();
        String[] locations = {"London","Manchester","Other"};
        String location = locations[homeTeam.getLocation()];
        String output = String.format("%30s%6s%30s%15f%15s%30s", homeTeam.getTeamName()," vs",awayTeam.getTeamName(),distance,location, clash);
        return output;
    }
    
    public boolean isPaired(){
        if(homeTeam.getPairs().contains(awayTeam.getTeamName())){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * @param clash the clash to set
     */
   public void setClash(String clash) {
        this.clash = clash;
    }
   
    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
}
