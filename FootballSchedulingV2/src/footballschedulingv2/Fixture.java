
package footballschedulingv2;
public class Fixture {
    private Team homeTeam;
    private Team awayTeam;
    
    public Fixture(Team home, Team away){
        this.homeTeam = home;
        this.awayTeam = away;
    }
    public Fixture(Fixture f){
        homeTeam = new Team(f.homeTeam);
        awayTeam = new Team(f.awayTeam);
    }
    

    /**
     * @return the homeTeam
     */
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * @param homeTeam the homeTeam to set
     */
    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    /**
     * @return the awayTeam
     */
    public Team getAwayTeam() {
        return awayTeam;
    }

    /**
     * @param awayTeam the awayTeam to set
     */
    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }
    @Override
    public String toString(){
        String output = homeTeam.getTeamName() + " vs " + awayTeam.getTeamName();
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
}
