
import java.util.ArrayList;
import java.util.Random;

public class Problem {

    private Division[] divisions;
    private double cost;
    private double bestCost;
    int currentCycle;

    public Problem(Division[] divisions) {
        this.divisions = divisions;
        this.cost = getCost();
        this.bestCost = cost;
        this.currentCycle = 1;
    }

    public Problem(Problem p) {
        this.copy(p);
    }
    public boolean acceptMove(int costChange){
        boolean moveAccepted = false;
        
        return moveAccepted;
    }
    public void move() {
        Random rand = new Random();
        int selectDivision = rand.nextInt(4);
        Division division = divisions[selectDivision];
        Team[] teams = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int selectTeam = rand.nextInt(teams.length);
        Team team1 = teams[selectTeam];
        int selectMove = rand.nextInt(3);
        int highestCycle = division.getHighestCycle();
        int team1Cycle = team1.getCycle();
        Team team2 = new Team();
        int selectTeam2 =0;
        switch (selectMove) {
            case 0: //move mergeCycle
                if(highestCycle ==0){
                    
                }
                else{
                ArrayList<Cycle> otherCycles = new ArrayList<Cycle>();
                for (int i = 0; i <= highestCycle; i++) {
                    if (i == team1Cycle) {
                        continue;
                    } else {
                        otherCycles.add(cycles[i]);
                    }
                }
                int selectCycle = rand.nextInt(otherCycles.size());
                Cycle otherCycle = otherCycles.get(selectCycle);
                selectTeam2 = rand.nextInt(otherCycle.getCycleSize());
                team2 = otherCycle.getTeams().get(selectTeam2);
                merge(team1, team2, division);
                }
            case 1: //split Cycle
                Cycle cycle = cycles[team1Cycle];
                selectTeam2 = rand.nextInt(cycle.getCycleSize());
                boolean feasible = false;
                while (feasible == false) {
                    team2 = cycle.getTeams().get(selectTeam2);
                    feasible = isSplitFeasible(team1, team2, division);
                }
                split(team1, team2, division);
            case 2://rearrange cycle
                cycle = cycles[team1Cycle];
                selectTeam2 = rand.nextInt(cycle.getCycleSize());
                feasible = false;
                while (feasible == false) {
                    team2 = cycle.getTeams().get(selectTeam2);
                    feasible = isRearrangeFeasible(team1, team2, division);
                }
                rearrange(team1, team2, division);
        }

    }

    public double getCost() {
        double total = 0.0;
        for (int j = 0; j < divisions.length; j++) {
            Division division = divisions[j];
            Team[] schedule = division.getSchedule();
            int[][] distances = division.getDistances();
            for (int i = 0; i < schedule.length; i++) {
                int distance = distances[i][schedule[i].getNextTeam()];
                total += distance;
            }
        }
        return total;

    }

    public void merge(Team team1, Team team2, Division division) {
        System.out.println("Merge Cycle");
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int team1Cycle = team1.getCycle();
        int team2Cycle = team2.getCycle();
        int cycle1Length = cycles[team1Cycle].getSize();
        int cycle2Length = cycles[team2Cycle].getSize();
        team1.setNextTeam(team2.getTeamID());
        team2.setPrevTeam(team1.getTeamID());
        team4.setNextTeam(team3.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
        int newCycle;
        int oldCycle;
        if (team1Cycle > team2Cycle) {
            newCycle = team2Cycle;
            oldCycle = team1Cycle;
        } else {
            newCycle = team1Cycle;
            oldCycle = team2Cycle;
        }
        Team currentTeam = team1;
        int newCycleLength = cycle1Length + cycle2Length;
        for (int i = 0; i < newCycleLength; i++) {
            currentTeam.setCycle(newCycle);
            currentTeam.setCyclePosition(i);
            cycles[newCycle].addTeam(currentTeam);
            currentTeam = schedule[currentTeam.getNextTeam()];

        }
        cycles[oldCycle].clearList();
        currentCycle--;

    }

    public void split(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int oldCycle = team1.getCycle();

        currentCycle++;
        team1.setNextTeam(team2.getTeamID());
        team2.setPrevTeam(team1.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
        team4.setNextTeam(team3.getTeamID());
        Team currentTeam = team1;
        int position = 0;

        while (currentTeam.getNextTeam() != team1.getTeamID()) {
            currentTeam.setCyclePosition(position);
            position++;
        }
        position = 0;
        currentTeam = team3;
        while (currentTeam.getNextTeam() != team3.getNextTeam()) {
            currentTeam.setCycle(currentCycle);
            currentTeam.setCyclePosition(position);
            position++;
            cycles[oldCycle].removeTeam(currentTeam);
            cycles[currentCycle].addTeam(currentTeam);
        }

    }

    public void rearrange(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int[][] distances = division.getDistances();
        System.out.println("Rearrange cycle");
        Team team3 = schedule[team1.getNextTeam()];
        Team team4 = schedule[team2.getNextTeam()];
        int team1Position = team1.getCyclePosition();
        int team2Position = team2.getCyclePosition();
        int team3Position = team3.getCyclePosition();
        int team4Position = team4.getCyclePosition();
        int temp = team1.getNextTeam();
        int cycleLength = cycles[team1.getCycle()].getCycleSize();
        team1.setNextTeam(team4.getPrevTeam());
        team4.setPrevTeam(temp);
        temp = team2.getNextTeam();
        team2.setNextTeam(team3.getPrevTeam());
        team3.setPrevTeam(temp);
        Team currentTeam = team3;
        if (team2Position > team3Position) {
            for (int i = team3Position; i <= team2Position; i++) {
                temp = currentTeam.getPrevTeam();
                currentTeam.setPrevTeam(currentTeam.getNextTeam());
                currentTeam.setNextTeam(temp);
                currentTeam = schedule[currentTeam.getNextTeam()];
            }

        } else if (team3Position > team2Position) {
            currentTeam = team3;
            for (int i = team3Position; i < cycleLength + team2Position; i++) {
                temp = currentTeam.getPrevTeam();
                currentTeam.setPrevTeam(currentTeam.getNextTeam());
                currentTeam.setNextTeam(temp);
                currentTeam = schedule[currentTeam.getNextTeam()];
            }

        }
        currentTeam = team1;
        for (int i = 0; i < cycleLength; i++) {
            currentTeam.setCyclePosition(i);
            currentTeam = schedule[currentTeam.getNextTeam()];
        }
    }

    public boolean isRearrangeFeasible(Team team1, Team team2, Division division) {
        Boolean feasible = false;
        if (team1.getNextTeam() != team2.getTeamID() && team2.getNextTeam() != team1.getTeamID()) {
            feasible = true;
        }
        return feasible;
    }

    public boolean isSplitFeasible(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int oldCycle = team1.getCycle();
        int cycle1Length = 0;
        int cycle2Length = 0;
        int team1Position = team1.getCyclePosition();//get teams cycle position
        int team2Position = team2.getCyclePosition();
        int team3Position = team3.getCyclePosition();
        int team4Position = team4.getCyclePosition();
        boolean feasible = false;
        int cycleLength = cycles[team1.getCycle()].getCycleSize();

        if (team1Position > team2Position) {
            cycle1Length = team1Position - team2Position + 1;
            if (team4Position > team3Position) {
                cycle2Length = team4Position - team3Position + 1;
            } else if (team3Position > team4Position) {
                cycle2Length = team4Position - team3Position + 1 + cycleLength;
            }
        } else if (team2Position > team1Position) {
            cycle1Length = team1Position + cycleLength + 1 - team2Position;
            cycle2Length = team4Position - team3Position;
        }
        if (cycle1Length % 2 == 0 && cycle1Length >= 4 && cycle2Length % 2 == 0 && cycle2Length >= 4) {
            feasible = true;//check feasibility
        }
        return feasible;

    }

    public void copy(Problem p) {

    }

}
